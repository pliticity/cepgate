(function(){
	'use strict';  
	var documentApp = angular.module('document', ['ui.bootstrap', 'ngAside', 'ngFileUpload', 'angularUtils.directives.dirPagination', 'ngTagsInput', 'ngDragDrop','ngCookies']);

	documentApp.config(['$httpProvider', function($httpProvider) {
		$httpProvider.defaults.withCredentials = true;
		$httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
		$httpProvider.defaults.headers.common['Access-Control-Allow-Headers'] = 'X-Requested-With,Set-Cookie,Set-Cookie2,Origin,Accept,Content-Type,A-TOKEN';
		$httpProvider.defaults.headers.common['Access-Control-Expose-Headers'] = 'X-Requested-With,Set-Cookie,Set-Cookie2,Origin,Accept,Content-Type,A-TOKEN';
		$httpProvider.defaults.headers.common['Access-Control-Allow-Credentials'] = 'true';
	}]);
	
	 documentApp.directive('onFinishRender', function ($timeout) {
	    return {
	        restrict: 'A',
	        link: function (scope, element, attr) {
	            if (scope.$last === true) {
	                $timeout(function () {
	                	$(".multipleitems").multiselect({includeSelectAllOption: true}); 
	                });
	            }
	        }
	    }
	});
	 
	 angular.module('document').controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, files) {

		  $scope.files = files;
		  /*$scope.selected = {
		    item: $scope.items[0]
		  };*/

		  $scope.ok = function () {
		    $uibModalInstance.close($scope.selected.item);
		  };

		  $scope.cancel = function () {
		    $uibModalInstance.dismiss('cancel');
		  };
		});	 
	 
	 function DataReloadWithAjaxCtrl(DTOptionsBuilder, DTColumnBuilder) {
		    
		 documentApp.recentDtOptions = DTOptionsBuilder.fromSource('/documents/loadDocuments?type=recent');
		 documentApp.searchDtOptions = DTOptionsBuilder.fromSource('/documents/loadDocuments?type=recent');
		    
		 documentApp.recentDtColumns = [
	        DTColumnBuilder.newColumn('id').withTitle('ID'),
	        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
	        DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
	    ];
		 documentApp.searchDtColumns = [
	        DTColumnBuilder.newColumn('id').withTitle('ID'),
	        DTColumnBuilder.newColumn('firstName').withTitle('First name'),
	        DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
	    ];
		                		        
		 documentApp.reloadData = reloadData;
		 documentApp.recentDtInstance = {};
		 documentApp.searchDtInstance = {};
		 $("#searchTable").css("width", "100%");
		$("#recentTable").css("width", "100%");
		
	    function reloadData() {
	        var resetPaging = false;
	        documentApp.recentDtInstance.reloadData(callback, resetPaging);
	        documentApp.searchDtInstance.reloadData(callback, resetPaging);
	    }

	    function callback(json) {
	        console.log(json);
	    }
	    reloadData();
	}
	 
	 documentApp.config(['$compileProvider',
         function ($compileProvider) {
             $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
     }]);

	
	 
	 $('#add-contact').click(function(e) { 
		 e.preventDefault();
	     var id = $(".nav-tabs").children().length; //think about it ;)
	     $(this).closest('li').before('<li><a href="#edit_'+id+'">Edit Document</a><span>x</span></li>');         
	     $('.tab-content').append('<div class="tab-pane" id="edit_'+id+'">'+ $("#document-edit").html() + '</div>');
	});
})();
 
 

//Use x-www-form-urlencoded Content-Type 
 /**
  * The workhorse; converts an object to x-www-form-urlencoded serialization.
  * @param {Object} obj
  * @return {String}
  */ 
 var param = function(obj) {
   var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
     
   for(name in obj) {
     value = obj[name];
       
     if(value instanceof Array) {
       for(i=0; i<value.length; ++i) {
         subValue = value[i];
         fullSubName = name + '[' + i + ']';
         innerObj = {};
         innerObj[fullSubName] = subValue;
         query += param(innerObj) + '&';
       }
     }
     else if(value instanceof Object) {
       for(subName in value) {
         subValue = value[subName];
         fullSubName = name + '[' + subName + ']';
         innerObj = {};
         innerObj[fullSubName] = subValue;
         query += param(innerObj) + '&';
       }
     }
     else if(value !== undefined && value !== null)
       query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
   } 
   return query.length ? query.substr(0, query.length - 1) : query;
 };


 
 
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

function hasClass(el, className) {
	  if (el.classList)
	    return el.classList.contains(className)
	  else
	    return !!el.className.match(new RegExp('(\\s|^)' + className + '(\\s|$)'))
	}

	function addClass(el, className) {
	  if (el && el.classList)
	    el.classList.add(className)
	  else if (!hasClass(el, className)) el.className += " " + className
	}

	function removeClass(el, className) {
	  if (el.classList)
	    el.classList.remove(className)
	  else if (hasClass(el, className)) {
	    var reg = new RegExp('(\\s|^)' + className + '(\\s|$)')
	    el.className=el.className.replace(reg, ' ')
	  }
	}
	
function clone(obj) {
    var copy;

    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        copy = [];
        for (var i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
}

 