
$(document).ready(function () {
    callback(moment(), moment());
});

$('#date-range-picker').daterangepicker({
    startDate: moment(),
    endDate: moment(),
    ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    }
}, callback);


function callback(start, end) {
    $('#date-range-picker span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));

    let request = openRestHttpPostRequest("/absences/chart");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            buildChart(response.responseData['chart']);
        }
    }
    request.send(JSON.stringify({
        "startDate": start,
        "endDate": end
    }));
}

function buildChart(chart) {

    let pieColors = (function () {
        let colors = [],
            base = Highcharts.getOptions().colors[0],
            i;

        for (i = 0; i < 10; i += 1) {
            // Start out with a darkened base color (negative brighten), and end
            // up with a much brighter color
            colors.push(Highcharts.color(base).brighten((i - 3) / 7).get());
        }
        return colors;
    }());

    Highcharts.chart('capacity-chart', {
        chart: {
            backgroundColor: 'transparent',
            plotBackgroundColor: 'transparent',
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Employee capacity chart'
        },
        tooltip: {
            pointFormat: '{point.sectionName}: <b>{point.percentage:.1f}%</b>'
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
                colors: pieColors,
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.sectionName}</b><br>{point.percentage:.1f} %',
                    distance: -50,
                    filter: {
                        property: 'percentage',
                        operator: '>',
                        value: 4
                    }
                }
            }
        },
        series: [{
            name: 'Share',
            data: chart['sections']
        }]
    });
}