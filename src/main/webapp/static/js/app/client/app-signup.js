(function(){
	angular.module('document').controller('SignupController', ['$http', '$scope', function($http, $scope, Upload){
		 var signup = this;
		 var messageDiv = angular.element( document.querySelector( '#messageDiv' ) );
		  $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
		  // Override $http service's default transformRequest
		  $http.defaults.transformRequest = [function(data) {
		    return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
		  }];
		 $scope.countries = [{"name": "Loading", "abbr": "Loading"}];
		 $scope.message = "";
		 signup.user = {"_csrf": "", "country": "", "company": "", "password": "", "email": "", "lastName": "", "firstName": ""};
		 $http.get('../json/countries.json').success(function(response){
			$scope.countries = response;
			 
		});
		 
		$scope.showMessage = function(){
			 return $scope.message.length > 0;
		}
		 
		$scope.auth = function(){ 
			 if(!$scope.authForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/signup/login',  
					 data: {username: signup.user.email, password: signup.user.passwd },
					 headers: {
						   'Accept': 'application/json',
						   "X-Requested-With": 'XMLHttpRequest',
					 },
				}).success(function(response){
					 console.log(response);	
					  
					window.location = "/member/index"; 
				}).error(function(response){
					if(response.error){
						messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						$scope.message = response.message;
					}else {
						messageDiv.removeClass("alert-success");
						messageDiv.addClass("alert-danger");
						$scope.message = response; 
					} 
				}); 
			 }
		 }
			 
		 $scope.signup = function(){ 
			 signup.user._csrf = $("#_csrf").val();
			 if(!$scope.signupForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/signup/signup',  
					 data: signup.user
				 }).success(function(response){
					 if(response.result == "error"){
						 messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						 $scope.message = response.message;
					 }else {
						 messageDiv.addClass("alert-success");
						 messageDiv.removeClass("alert-danger");
						 window.location = "/signup/login";
					 }
				}).error(function(response){
					messageDiv.removeClass("alert-success");
					messageDiv.addClass("alert-danger");
					$scope.message = response; 
				});
				 
			 }
		 }
		 
	 }]);
})();
 

$(".nav-pills").on("click", "a", function(e){
    e.preventDefault();
    $(this).tab('show');
  })
  .on("click", "span", function () {
      var anchor = $(this).siblings('a');
      $(anchor.attr('href')).remove();
      $(this).parent().remove();
      $(".nav-tabs li").children('a').first().click();
      $('#home').click();
  });

function renderMultiSelect(){
	  console.log("test");
}
function padDigits(number, digits) {
    return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}