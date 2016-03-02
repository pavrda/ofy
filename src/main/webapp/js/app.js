var myApp = angular.module('myApp', [
  'ngRoute'
]);

myApp.config(['$routeProvider',
                  function($routeProvider) {
                    $routeProvider.
                      when('/nedele', {
                        templateUrl: 'tplNedele.html',
                        controller: 'nedeleController'
                      }).
                      when('/blabla', {
                          templateUrl: 'blablaCategory.html',
                          controller: 'blablaCtrl'
                        }).
                      otherwise({
                          templateUrl: 'tplHome.html',
                          controller: 'emptyController'
                      });
                  }]);

