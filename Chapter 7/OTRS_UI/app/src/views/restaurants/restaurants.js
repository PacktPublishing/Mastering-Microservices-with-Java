'use strict';
angular.module('otrsApp.restaurants', [
    'ui.router',
    'ui.bootstrap',
    'ngStorage',
    'ngResource'
])
        .config([
            '$stateProvider', '$urlRouterProvider', '$httpProvider',
            function ($stateProvider, $urlRouterProvider, $httpProvider) {
                $stateProvider.state('restaurants', {
                    url: '/restaurants',
                    templateUrl: 'restaurants/restaurants.html',
                    controller: 'RestaurantsCtrl'
                })
                        // Restaurant show page
                        .state('restaurants.profile', {
                            url: '/:id',
                            views: {
                                '@': {
                                    templateUrl: 'restaurants/restaurant.html',
                                    controller: 'RestaurantCtrl'
                                }
                            }
                        });
                $httpProvider.defaults.useXDomain = true;
                delete $httpProvider.defaults.headers.common['X-Requested-With'];
            }])
        .factory('restaurantService', function ($injector, $q) {
            var log = $injector.get('$log');
            var ajaxHandler = $injector.get('AjaxHandler');
            var $http = $injector.get('$http');
            var deffered = $q.defer();
            var restaurantService = {};
            restaurantService.restaurants = [];
            restaurantService.orignalRestaurants = [];
            restaurantService.async = function () {
                ajaxHandler.startSpinner();
                //if (restaurantService.restaurants.length === 0) {
                $http.get(ajaxHandler.getRestBaseUrl() + '/restaurants/')
                        .then(
                                function (response) {
                                    log.debug('setting restaurants from restaurant service');
                                    sessionStorage.apiActive = true;
                                    restaurantService.restaurants = response.data;
                                    ajaxHandler.stopSpinner();
                                    deffered.resolve();
                                },
                                function (response) {
                                    if (response.status <= 0) {
                                        restaurantService.async()
                                    }
                                    restaurantService.restaurants = mockdata;
                                    ajaxHandler.stopSpinner();
                                    deffered.resolve();
                                });
                return deffered.promise;
                /*} else {
                 deffered.resolve();
                 ajaxHandler.stopSpinner();
                 return deffered.promise;
                 }*/
            };
            restaurantService.list = function () {
                return restaurantService.restaurants;
            };
            restaurantService.add = function () {
                console.log("called add");
                restaurantService.restaurants.push(
                        {
                            id: 103,
                            name: 'Chi Cha\'s Noodles',
                            address: '13 W. St., Eastern Park, New County, Chicago, IL 60654',
                        });
            };
            restaurantService.search = function (searchedValue, callback) {
                ajaxHandler.startSpinner();
                if (!searchedValue) {
                    if (restaurantService.orignalRestaurants.length > 0) {
                        restaurantService.restaurants = restaurantService.orignalRestaurants;
                    }
                    deffered.resolve();
                    ajaxHandler.stopSpinner();
                    return deffered.promise;
                } else {
                    $http.get(ajaxHandler.getRestBaseUrl() + '/restaurants?name=' + searchedValue)
                            .then(function (response) {
                                log.debug('Searching restaurants...');
                                sessionStorage.apiActive = true;
                                log.debug("Search found " + response.data.length + " restaurants.");
                                if (restaurantService.orignalRestaurants.length < 1) {
                                    restaurantService.orignalRestaurants = restaurantService.restaurants;
                                }
                                restaurantService.restaurants = response.data;
                                ajaxHandler.stopSpinner();
                                callback(restaurantService.restaurants);
                                deffered.resolve();
                            }, function (response) {
                                if (restaurantService.orignalRestaurants.length < 1) {
                                    restaurantService.orignalRestaurants = restaurantService.restaurants;
                                }
                                restaurantService.restaurants = [];
                                ajaxHandler.stopSpinner();
                                deffered.resolve();
                            });
                    return deffered.promise;
                }
            };
            return restaurantService;
        })
        .controller('RestaurantsCtrl', function ($scope, restaurantService) {
            $scope.restaurantService = restaurantService;
            if ($scope.$parent.searchedValue) {
                $scope.$parent.searchedValue = "";
            } else {
                restaurantService.async().then(function () {
                    $scope.restaurants = restaurantService.list();
                });
            }
        })
        .controller('RestaurantCtrl', function ($scope, $state, $stateParams, $injector, restaurantService) {
            var $sessionStorage = $injector.get('$sessionStorage');
            $scope.format = 'dd MMMM yyyy';
            $scope.today = $scope.dt = new Date();
            $scope.dateOptions = {
                formatYear: 'yy',
                maxDate: new Date().setDate($scope.today.getDate() + 180),
                minDate: $scope.today.getDate(),
                startingDay: 1
            };

            $scope.popup1 = {
                opened: false
            };
            $scope.altInputFormats = ['M!/d!/yyyy'];
            $scope.open1 = function () {
                $scope.popup1.opened = true;
            };
            $scope.hstep = 1;
            $scope.mstep = 30;

            if ($sessionStorage.reservationData) {
                $scope.restaurant = $sessionStorage.reservationData.restaurant;
                $scope.dt = new Date($sessionStorage.reservationData.tm);
                $scope.tm = $scope.dt;
            } else {
                $scope.dt.setDate($scope.today.getDate() + 1);
                $scope.tm = $scope.dt;
                $scope.tm.setHours(19);
                $scope.tm.setMinutes(30);
                angular.forEach(restaurantService.list(), function (value, key) {
                    if (value.id === $stateParams.id) {
                        $scope.restaurant = value;
                    }
                });
            }
            $scope.book = function () {
                var tempHour = $scope.tm.getHours();
                var tempMinute = $scope.tm.getMinutes();
                $scope.tm = $scope.dt;
                $scope.tm.setHours(tempHour);
                $scope.tm.setMinutes(tempMinute);
                if ($sessionStorage.currentUser) {
                    console.log("$scope.tm --> " + $scope.tm);
                    var ajaxHandler = $injector.get('AjaxHandler');
                    var $http = $injector.get('$http');
                    var log = $injector.get('$log');
                    var postData = {};
                    postData.restaurantId = $scope.restaurant.id;
                    postData.userId = $sessionStorage.currentUser;
                    postData.date = $scope.dt;
                    postData.time = [$scope.tm.getHours(), $scope.tm.getMinutes(), $scope.tm.getSeconds(), $scope.tm.getMilliseconds()];

                    ajaxHandler.startSpinner();
                    $http.post(ajaxHandler.getRestBaseUrl() + '/bookings/', postData)
                            .then(function (response) {
                                log.debug('reserving table...');
                                sessionStorage.apiActive = true;
                                log.debug("Booking confirmed with id --> " + response.data.id);
                                alert("Booking Confirmed!!!\nRedirecting back to home page.");
                                ajaxHandler.stopSpinner();
                                $state.go("restaurants");
                            }, function (response) {
                                ajaxHandler.stopSpinner();
                            });
                    $sessionStorage.reservationData = null;
                } else {
                    $sessionStorage.reservationData = {};
                    $sessionStorage.reservationData.restaurant = $scope.restaurant;
                    $sessionStorage.reservationData.tm = $scope.tm;
                    $state.go("login");
                }
            }
        })
        .filter('date1', function ($filter) {
            return function (argDate) {
                if (argDate) {
                    var d = $filter('date')(new Date(argDate), 'dd MMM yyyy');
                    return d.toString();
                }
                return "";
            };
        })
        .filter('time1', function ($filter) {
            return function (argTime) {
                if (argTime) {
                    return $filter('date')(new Date(argTime), 'HH:mm:ss');
                }
                return "";
            };
        })
        .filter('datetime1', function ($filter) {
            return function (argDateTime) {
                if (argDateTime) {
                    return $filter('date')(new Date(argDateTime), 'dd MMM yyyy HH:mm a');
                }
                return "";
            };
        });

var mockdata = [];
mockdata.push(
        {
            id: "1",
            name: 'Le Meurice',
            address: '228 rue de Rivoli, 75001, Paris'
        });
mockdata.push(
        {
            id: "2",
            name: 'L\'Ambroisie',
            address: '9 place des Vosges, 75004, Paris'
        });
mockdata.push(
        {
            id: "3",
            name: 'Arpège',
            address: '84, rue de Varenne, 75007, Paris'
        });
mockdata.push(
        {
            id: "4",
            name: 'Alain Ducasse au Plaza Athénée',
            address: '25 avenue de Montaigne, 75008, Paris'
        });
mockdata.push(
        {
            id: "5",
            name: 'Pavillon LeDoyen',
            address: '1, avenue Dutuit, 75008, Paris'
        });
mockdata.push(
        {
            id: "6",
            name: 'Pierre Gagnaire',
            address: '6, rue Balzac, 75008, Paris'
        });
mockdata.push(
        {
            id: "7",
            name: 'L\'Astrance',
            address: '4, rue Beethoven, 75016, Paris'
        });
mockdata.push(
        {
            id: "8",
            name: 'Pré Catelan',
            address: 'Bois de Boulogne, 75016, Paris'
        });
mockdata.push(
        {
            id: "9",
            name: 'Guy Savoy',
            address: '18 rue Troyon, 75017, Paris'
        });
mockdata.push(
        {
            id: "10",
            name: 'Le Bristol',
            address: '112, rue du Faubourg St Honoré, 8th arrondissement, Paris'
        });
