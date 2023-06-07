$(function() {
    var id = $('#container-static').attr('value');
    $.getJSON('/bank/statistic?clid='+id, function(data) {
        var chartData = [];
        $.each(data, function(index, value) {
            chartData.push({
                name: value.description,
                y: value.percentage
            });
        });
        Highcharts.chart('container-static', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '',
                align: 'left'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                },
            },
            series: [{
                name: 'Метка проверки',
                colorByPoint: true,
                data: chartData
            }]
        });
    });
});