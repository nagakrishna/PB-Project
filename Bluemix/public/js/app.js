var myApp = angular.module('myapp',['ngRoute', 'ngAnimate']);

myApp.config(function($routeProvider){
    $routeProvider
        .when('/',{
            templateUrl: 'templates/craze.html',
            controller: 'crazeController'
        })
        // .when('/CrazeAnalysis',{
        //     templateUrl: 'craze.html',
        //     controller: 'crazeController'
        // })
        .when('/LocationAnalysis',{
            templateUrl: 'templates/location.html',
            controller: 'LocationController'
        })
        .when('/LanguageAnalysis',{
            templateUrl: 'templates/language.html',
            controller: 'LanguageController'
        })
        .when('/SourceAnalysis',{
            templateUrl: 'templates/source.html',
            controller: 'SourceController'
        })
        .when('/CelebAnalysis',{
            templateUrl: 'templates/celeb.html',
            controller: 'CelebController'
        })
        .when('/DayAnalysis',{
            templateUrl: 'templates/day.html',
            controller: 'DayController'
        })
        .when('/StatsAnalysis',{
            templateUrl: 'templates/stats.html',
            controller: 'StatsController'
        })
        .when('/TimezoneAnalysis',{
            templateUrl: 'templates/timezone.html',
            controller: 'TimezoneController'
        })
    .otherwise({ redirectTo: '/' });

});

myApp.controller('TimezoneController', function ($scope, $http, $location) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query8?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        var chart = AmCharts.makeChart("chartdiv", {
            "theme": "light",
            "type": "serial",
            "startDuration": 2,
            "dataProvider": [{
                "country": resultArray[0].timezone,
                "visits": resultArray[0].count,
                "color": "#FF0F00"
            }, {
                "country": resultArray[1].timezone,
                "visits": resultArray[1].count,
                "color": "#FF6600"
            }, {
                "country": resultArray[2].timezone,
                "visits": resultArray[2].count,
                "color": "#FF9E01"
            }, {
                "country": resultArray[3].timezone,
                "visits": resultArray[3].count,
                "color": "#FCD202"
            }, {
                "country": resultArray[4].timezone,
                "visits": resultArray[4].count,
                "color": "#F8FF01"
            }, {
                "country": resultArray[5].timezone,
                "visits": resultArray[5].count,
                "color": "#B0DE09"
            }, {
                "country": resultArray[6].timezone,
                "visits": resultArray[6].count,
                "color": "#04D215"
            }, {
                "country": resultArray[7].timezone,
                "visits": resultArray[7].count,
                "color": "#0D8ECF"
            }, {
                "country": resultArray[8].timezone,
                "visits": resultArray[8].count,
                "color": "#0D52D1"
            }, {
                "country": resultArray[9].timezone,
                "visits": resultArray[9].count,
                "color": "#2A0CD0"
            }],
            "graphs": [{
                "balloonText": "[[category]]: <b>[[value]]</b>",
                "colorField": "color",
                "fillAlphas": 0.85,
                "lineAlpha": 0.1,
                "type": "column",
                "topRadius":1,
                "valueField": "visits"
            }],
            "depth3D": 40,
            "angle": 30,
            "chartCursor": {
                "categoryBalloonEnabled": false,
                "cursorAlpha": 0,
                "zoomable": false
            },
            "categoryField": "country",
            "categoryAxis": {
                "gridPosition": "start",
                "axisAlpha":0,
                "gridAlpha":0

            },
            "export": {
                "enabled": true
            }

        },0);

        jQuery('.chart-input').off().on('input change',function() {
            var property	= jQuery(this).data('property');
            var target		= chart;
            chart.startDuration = 0;

            if ( property == 'topRadius') {
                target = chart.graphs[0];
            }

            target[property] = this.value;
            chart.validateNow();
        });
    })
})
myApp.controller('StatsController', function ($scope, $http, $location) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query7?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        var chart = AmCharts.makeChart( "chartdiv", {
            "type": "funnel",
            "theme": "light",
            "dataProvider": resultArray,
            "balloon": {
                "fixedPosition": true
            },
            "valueField": "count",
            "titleField": "name",
            "marginRight": 240,
            "marginLeft": 50,
            "startX": -500,
            "depth3D": 100,
            "angle": 40,
            "outlineAlpha": 1,
            "outlineColor": "#FFFFFF",
            "outlineThickness": 2,
            "labelPosition": "right",
            "balloonText": "[[name]]: [[count]]n[[description]]",
            "export": {
                "enabled": true
            }
        } );
        jQuery( '.chart-input' ).off().on( 'input change', function() {
            var property = jQuery( this ).data( 'property' );
            var target = chart;
            var value = Number( this.value );
            chart.startDuration = 0;

            if ( property == 'innerRadius' ) {
                value += "%";
            }

            target[ property ] = value;
            chart.validateNow();
        } );
    })

});
myApp.controller('CelebController', function ($scope, $http, $location, $sce) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query5?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        var m = document.getElementById("first");
        m.style.display = "block"
        $scope.name1 = resultArray[0].name;
        $scope.name2 = resultArray[1].name;
        $scope.name3 = resultArray[2].name;
        $scope.name4 = resultArray[3].name;
        $scope.name5 = resultArray[4].name;
        $scope.name6 = resultArray[5].name;
        $scope.name7 = resultArray[6].name;
        $scope.name8 = resultArray[7].name;
        $scope.name9 = resultArray[8].name;
        $scope.name10 = resultArray[9].name;

        $scope.button1 = function () {
            $scope.naga = resultArray[0].imageUrl;
            $scope.width = "150px";
            // $scope.Icon = "<img src=" + resultArray[0].imageUrl + "style=\"width: 200px; height: 200px\"/>";
            // var t = document.getElementsByClassName("Icon");
            // t.style.width = "200px";
            // t.style.height = "200px"
            // <img src="app.js" style="width: 200px; height: 200px">
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[0].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[0].count + "</p>";
        }
        $scope.button2 = function () {
            $scope.naga = resultArray[1].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[1].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[1].count + "</p>";
        }
        $scope.button3 = function () {
            $scope.naga = resultArray[2].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[2].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[2].count + "</p>";
        }
        $scope.button4 = function () {
            $scope.naga = resultArray[3].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[3].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[3].count + "</p>";
        }
        $scope.button5 = function () {
            $scope.naga = resultArray[4].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[4].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[4].count + "</p>";
        }
        $scope.button6 = function () {
            $scope.naga = resultArray[5].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[5].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[5].count + "</p>";
        }
        $scope.button7 = function () {
            $scope.naga = resultArray[6].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[6].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[6].count + "</p>";
        }
        $scope.button8 = function () {
            $scope.naga = resultArray[7].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[7].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[7].count + "</p>";
        }
        $scope.button9 = function () {
            $scope.naga = resultArray[8].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[8].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[8].count + "</p>";
        }
        $scope.button10 = function () {
            $scope.naga = resultArray[9].imageUrl;
            $scope.width = "150px";
            $scope.description = "<p style='font-weight: 700; display: inline'> Description: </p>" + "<p style='display: inline'>" + resultArray[9].description + "</p>";
            $scope.followersCount = "<p style='font-weight: 700; display: inline'> Followers Count: </p>" + "<p style='display: inline'>" + resultArray[9].count + "</p>";
        }
        $scope.renderHtml = function (htmlCode) {
            return $sce.trustAsHtml(htmlCode);
        };

    })
    });

myApp.controller('DayController', function ($scope, $http, $location) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query6?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        var chart = AmCharts.makeChart( "chartdiv", {
            "type": "pie",
            "theme": "light",
            "dataProvider": resultArray,
            "valueField": "count",
            "titleField": "day",
            "outlineAlpha": 0.4,
            "depth3D": 15,
            "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
            "angle": 30,
            "export": {
                "enabled": true
            }
        } );
        jQuery( '.chart-input' ).off().on( 'input change', function() {
            var property = jQuery( this ).data( 'property' );
            var target = chart;
            var value = Number( this.value );
            chart.startDuration = 0;

            if ( property == 'innerRadius' ) {
                value += "%";
            }

            target[ property ] = value;
            chart.validateNow();
        } );
    })
});

myApp.controller('crazeController', function ($scope, $http, $window, $location) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query2?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {


        resultArray1 = data[0];
        resultArray = resultArray1.result;

        total = resultArray[0].total;
        trump = resultArray[0].trump;
        sanders = resultArray[0].sanders;
        clinton = resultArray[0].clinton;
        cruz = resultArray[0].cruz;
        john = resultArray[0].john;

        var data = [{
            values: [trump, sanders, clinton, cruz, john],
            labels: ['Donald Trump', 'Bernie Sanders', 'Hillary Clinton', 'Ted Cruz', 'John Kasich'],
            type: 'pie'
        }];

        var layout = {
            height: 420,
            width: 680
        };

        Plotly.newPlot('myDiv', data, layout);
    })

});

myApp.controller('LocationController', function ($scope, $http, nagafactory, $location) {
    nagafactory.getData()
        .then(function(data){
            resultArray1 = data[0];
            resultArray2 = resultArray1.result;
            l = resultArray2.length;
            var mapOptions = {
                zoom: 2,
                center: new google.maps.LatLng(0, 0),
                mapTypeId: google.maps.MapTypeId.ROADMAP
            }
            $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);
            for (i = 0; i < l; i++){
                createMarker(resultArray2[i])
            }
        });

    $scope.markers = [];
    var infoWindow = new google.maps.InfoWindow();
    var createMarker = function (info){
        var marker = new google.maps.Marker({
            map: $scope.map,
            position: new google.maps.LatLng(Number(info.lat), Number(info.lon)),
            // position: new google.maps.LatLng(naga1[i], naga2[i]),
            title: info.name,
            imageUrl: info.imageUrl,
            icon: "https://g.twimg.com/twitter-bird-16x16.png",
            content: info.text
        });
        // marker.content = '<div class="infoWindowContent">' + info.text + '</div>';

        google.maps.event.addListener(marker, 'click', function(){
            infoWindow.setContent('<div id="container" style="white-space:nowrap"><div style="display: inline; float: right "><img src=' + marker.imageUrl + '></div> <div style="display: inline"> <h2>' + marker.title + '</h2></div>' + '</div>' + marker.content);
            infoWindow.open($scope.map, marker);
        });

        $scope.markers.push(marker);

    }

    $scope.openInfoWindow = function(e, selectedMarker){
        e.preventDefault();
        google.maps.event.trigger(selectedMarker, 'click');
    }
});
    myApp.factory('nagafactory', function($http, $log, $q){
    return{
        getData: function(){
            var deferred = $q.defer();
            $http.get("https://api.mlab.com/api/1/databases/pb/collections/query1?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x")
                .success(function (data){
                    deferred.resolve(data);
                })
                .error(function (msg, code){
                    deferred.reject(msg);
                    $log.error(msg, code);
                });
            return deferred.promise;

        }
    }
});

myApp.controller('home', function ($scope, $http, $location) {
    $scope.currentPage = "CrazeAnalysis";
        $scope.go = function (page) {
            $location.path('/' + page);
        };
});

myApp.controller('LanguageController', function ($scope, $http, $location) {

    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query3?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        Chart.types.Line.extend({
            name: "LineAlt",
            draw: function () {
                Chart.types.Line.prototype.draw.apply(this, arguments);

                var ctx = this.chart.ctx;
                ctx.save();
                // text alignment and color
                ctx.textAlign = "center";
                ctx.textBaseline = "bottom";
                ctx.fillStyle = this.options.scaleFontColor;
                // position
                var x = this.scale.xScalePaddingLeft * 0.4;
                var y = this.chart.height / 2;
                // change origin
                ctx.translate(x, y)
                // rotate text
                ctx.rotate(-90 * Math.PI / 180);
                ctx.fillText(this.datasets[0].label, 0, 0);
                ctx.restore();
            }
        });

        var data = {
            labels: [resultArray[9].lang, resultArray[7].lang, resultArray[5].lang, resultArray[3].lang,
                resultArray[1].lang, resultArray[0].lang,resultArray[2].lang, resultArray[4].lang,
                resultArray[6].lang, resultArray[8].lang,],
            datasets: [
                {
                    label: "Tweet Count",
                    fillColor: "rgba(151,187,205,0.2)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: [resultArray[9].count, resultArray[7].count, resultArray[5].count, resultArray[3].count,
                        resultArray[1].count, resultArray[0].count,resultArray[2].count, resultArray[4].count,
                        resultArray[6].count, resultArray[8].count]
                }
            ]
        };

        var ctx = document.getElementById("myChart").getContext("2d");
        var myLineChart = new Chart(ctx).LineAlt(data, {
            // make enough space on the right side of the graph
            scaleLabel: "          <%=value%>"
        });

    })


});

myApp.controller('SourceController', function ($scope, $location, $http) {
    $http.get("https://api.mlab.com/api/1/databases/pb/collections/query4?apiKey=FyQstvzQY5JPPyMXWs6C7kbUUrILDM-x").success(function(data) {
        resultArray1 = data[0];
        resultArray = resultArray1.result;
        var data = [
            {
                x: [resultArray[9].source, resultArray[7].source, resultArray[5].source, resultArray[3].source,
                    resultArray[1].source,resultArray[0].source, resultArray[2].source, resultArray[4].source,
                    resultArray[6].source, resultArray[8].source],

                y: [ resultArray[9].count, resultArray[7].count, resultArray[5].count, resultArray[3].count,
                     resultArray[1].count, resultArray[0].count, resultArray[2].count, resultArray[4].count,
                     resultArray[6].count, resultArray[8].count],
                type: 'bar'
            }
        ];

        Plotly.newPlot('myDiv', data);


    })
});
