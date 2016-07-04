'use strict';

var otrsApp = angular.module('otrsApp', [
    'ui.router',
    'templates',
    'ui.bootstrap',
    'ngStorage',
    'otrsApp.httperror',
    'otrsApp.login',
    'otrsApp.restaurants'
])
        .config([
            '$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {
                $urlRouterProvider.otherwise('/restaurants');
            }])

        .controller('otrsAppCtrl', function ($state, $scope, $rootScope, $injector, restaurantService) {
            var controller = this;

            var AjaxHandler = $injector.get('AjaxHandler');
            var $rootScope = $injector.get('$rootScope');
            var log = $injector.get('$log');
            var sessionStorage = $injector.get('$sessionStorage');
            $scope.showSpinner = false;

            $scope.search = function () {
                $scope.restaurantService = restaurantService;
                restaurantService.search($scope.searchedValue,
                        function (data) {
                            $scope.restaurants = data;
                        });
                if ($state.current.name !== 'restaurants') {
                    $state.go("restaurants", $rootScope.fromStateParams)
                }
            }

            $scope.$on('$destroy', function destroyed() {
                log.debug('otrsAppCtrl destroyed');
                controller = null;
                $scope = null;
            });

            $rootScope.fromState;
            $rootScope.fromStateParams;
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromStateParams) {
                $rootScope.fromState = fromState;
                $rootScope.fromStateParams = fromStateParams;
            });

            // utility method
            $scope.isLoggedIn = function () {
                if (sessionStorage.session) {
                    return true;
                } else {
                    return false;
                }
            };

            /* spinner status */
            $scope.isSpinnerShown = function () {
                return AjaxHandler.getSpinnerStatus();
            };

        })
        .run(['$rootScope', '$injector', '$state', function ($rootScope, $injector, $state) {
                $rootScope.restaurants = null;
                // self reference
                var controller = this;
                // inject external references
                var log = $injector.get('$log');
                var $sessionStorage = $injector.get('$sessionStorage');
                var AjaxHandler = $injector.get('AjaxHandler');

                if (sessionStorage.currentUser) {
                    $rootScope.currentUser = $sessionStorage.currentUser;
                } else {
                    $rootScope.currentUser = "Guest";
                    $sessionStorage.currentUser = ""
                }
            }])
