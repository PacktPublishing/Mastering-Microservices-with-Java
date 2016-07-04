angular.module('otrsApp.httperror', [
    'ui.router',
    'ngStorage'
])
        .config(function config($stateProvider) {
            $stateProvider.state('httperror', {
                url: '/httperror',
                //views: {
                //    "main": {
                controller: 'HttpErrorCtrl',
                templateUrl: 'httperror/httperror.html'
                        //    }
                        //},
                        //data: {pageTitle: 'HTTP Problem'}
            });
        })
        .controller('HttpErrorCtrl', ['$scope', '$injector', function ($scope, $injector) {
                var controller = this;

                // inject external references
                var log = $injector.get('$log');
                var sessionStorage = $injector.get('$sessionStorage');
                var state = $injector.get('$state');
                var timeout = $injector.get('$timeout');

                // life cycle
                $scope.$on('$viewContentLoaded', function contentLoaded() {
                    log.debug('HttpErrorCtrl content loaded');
                });
                $scope.$on('$destroy', function destroyed() {
                    log.debug('HttpErrorCtrl destroyed');
                    controller = null;  // against memory leaks
                    $scope = null;    // against memory leaks
                });

                // default on screen error message, possible updated
                $scope.errorMessage = "oh, had some problems fetching something from the API";

                // save the http status code
                var statusCode = sessionStorage.httpStatus;
                if (statusCode) {
                    if (statusCode >= 500) {
                        // Is the server even running?
                        $scope.errorMessage = "Please check the API server";
                    } else if (statusCode >= 400) {
                        // Something wrong with the session, probably expired
                        if (sessionStorage.session) {
                            // destroy any session objects
                            sessionStorage.session = '';
                            sessionStorage.authentication = '';

                            $scope.errorMessage = "Your session may have expired, please login again";
                            // redirect after pause
                            timeout(function () {
                                state.go('login');
                            }, 5000);
                        } else {
                            // destroy any authentication object
                            sessionStorage.authentication = '';
                            // redirect after pause
                            $scope.errorMessage = "Not sure what happened there, could you try that again?";
                            timeout(function () {
                                state.go('home');
                            }, 5000);
                        }
                    } else {
                        $scope.errorMessage = "Please check the API server is available";
                    }
                } else {
                    // destroy any session objects
                    sessionStorage.session = '';
                    sessionStorage.authentication = '';
                    // Is the server even running?
                    $scope.errorMessage = "Please check the API server is available";
                }
            }]);
