let home = {
    chart: null,
    dataList: null,
    noticeList: null,
    annotations: null,
    setDatepicker: function() {
        let nowDate = new Date();
        let e_year = nowDate.getFullYear();
        let e_month = nowDate.getMonth() + 1 < 10 ? '0' + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
        let e_date = nowDate.getDate() < 10 ? '0' + nowDate.getDate() : nowDate.getDate();
        nowDate.setDate(nowDate.getDate() - 14);
        let s_year = nowDate.getFullYear();
        let s_month = nowDate.getMonth() + 1 < 10 ? '0' + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
        let s_date = nowDate.getDate() < 10 ? '0' + nowDate.getDate() : nowDate.getDate();;

        $('#date-picker-div input[name=start]').val(s_year + '-' + s_month + '-' + s_date);
        $('#date-picker-div input[name=end]').val(e_year + '-' + e_month + '-' + e_date)
    },
    setMarketItem: function() {
        return $.ajax({
            type: 'get',
            url: '/api/v1/market-items',
            data: {
                categoryCode: $('#category-select').val()
            },
            dataType: 'json',
            beforeSend: function() {
                $('#item-select').empty();
            },
            success: function(result) {
                let $fragment = $(document.createDocumentFragment());
                for (let i=0; i<result.data.length; i++) {
                    let data = result.data[i];
                    $fragment.append($('<option>', {value: data.id, text: data.name}))
                }

                $('#item-select').append($fragment);
            }
        })
    },
    getNotices: function() {
        return $.ajax({
            type: 'get',
            url: '/api/v1/notices',
            data: {
                startDate: $('#date-picker-div input[name=start]').val(),
                endDate: $('#date-picker-div input[name=end]').val()
            },
            dataType: 'json',
            beforeSend: function() {},
            success: function(result) {
                home.noticeList = result.data;
                home.annotations = {};

                let map = new Map();
                for (let i=0; i<home.noticeList.length; i++) {
                    let notice = home.noticeList[i];

                    let dt = notice.date.split('T')[0];
                    if (map.has(dt)) {
                        map.get(dt).push(i+1);
                    } else {
                        let arr = [];
                        arr.push(i+1)
                        map.set(dt, arr);
                    }
                }

                map.forEach((value, key) => {
                    home.annotations['annotation_' + key] = {
                        type: 'line',
                        xMin: key,
                        xMax: key,
                        label: {
                            display: true,
                            content: value.join(',')
                        }
                    }
                });
            }
        });
    },
    getMarketItemDailyData: function () {
        return $.ajax({
            type: 'get',
            url: '/api/v1/market-items/' + $('#item-select').val() + '/data',
            data: {
                startDate: $('#date-picker-div input[name=start]').val(),
                endDate: $('#date-picker-div input[name=end]').val()
            },
            dataType: 'json',
            beforeSend: function() {
                $('#search-button').prop('disabled', true);
            },
            success: function(result) {
                home.dataList = result.data.dataList;
                $('#search-button').prop('disabled', false);
            }
        });
    },
    setDailyDataTable: function() {
        $('#daily-data-tbody').empty();
        $('#bundle-count-span').html(home.dataList[0].bundleCount);

        let $fragment = $(document.createDocumentFragment());
        for (let i=0; i<home.dataList.length; i++) {
            let data = home.dataList[i];

            let $tr = $('<tr>');
            let $td0 = $('<td>', { text: data.date.split('T')[0] });
            let $td1 = $('<td>', { text: data.avgPrice });
            let $td2 = $('<td>', { text: data.tradeCount });

            $tr.append($td0, $td1, $td2);
            $fragment.append($tr);
        }

        $('#daily-data-tbody').append($fragment);
    },
    setNoticeTable: function() {
        $('#notice-tbody').empty();

        let $fragment = $(document.createDocumentFragment());
        for (let i=0; i<home.noticeList.length; i++) {
            let data = home.noticeList[i];

            let $tr = $('<tr>');
            let $td0 = $('<td>', { text: i+1 });
            let $td1 = $('<td>', { text: data.date.split('T')[0] });
            let $td2 = $('<td>', { text: data.title });
            let $td3 = $('<td>');
            let $a = $('<a>', { href: data.link, target: '_blank', text: '바로 가기' });
            $td3.html($a);

            $tr.append($td0, $td1, $td2, $td3);
            $fragment.append($tr);
        }

        $('#notice-tbody').append($fragment);
    },
    drawGraph: function() {
        let barData = [];
        let lineData = [];
        for (let i=0; i<home.dataList.length; i++) {
            let data = home.dataList[i];

            barData.push({
                x: data.date.split('T')[0],
                y: data.tradeCount
            });
            lineData.push({
                x: data.date.split('T')[0],
                y: data.avgPrice
            });
        }

        home.chart.data.datasets[0].data = barData;
        home.chart.data.datasets[1].data = lineData;
        home.chart.options.plugins.annotation.annotations = home.annotations;

        home.chart.update();
    }
}

$(document).ready(function() {
    $('#nav-ul li a').removeClass('active');
    $('#nav-ul li:eq(0) a').addClass('active');

    $('#category-select').on('change', home.setMarketItem);

    home.setDatepicker();
    $('#date-picker-div').datepicker({
        language: "ko",
        format: 'yyyy-mm-dd',
        startDate: '2023-08-26',
        endDate: '+1d'
    });

    $('#search-button').on('click', function() {
        $.when(home.getNotices(), home.getMarketItemDailyData()).then(function() {
            home.setNoticeTable();
            home.setDailyDataTable();
            home.drawGraph();
        });
    });

    home.chart = new Chart(document.getElementById('daily-data-canvas'), {
        data: {
            datasets: [
                {
                    type: 'bar',
                    label: '거래량',
                    yAxisID: 'y0',
                    data: []
                },
                {
                    type: 'line',
                    label: '평균 가격',
                    yAxisID: 'y1',
                    data: []
                }
            ]
        },
        options: {
            interaction: {
                mode: 'index',
                intersect: false,
            },
            scales: {
                y0: {
                    display: true,
                    position: 'left',
                    title: {
                        display: true,
                        text: '거래량'
                    }
                },
                y1: {
                    display: true,
                    position: 'right',
                    title: {
                        display: true,
                        text: '가격'
                    }
                }
            },
            plugins: {
                annotation: {
                    annotations: {

                    }
                }
            }
        }
    });

    $.when(home.setMarketItem()).then(function() {
        $('#search-button').trigger('click');
    })
});