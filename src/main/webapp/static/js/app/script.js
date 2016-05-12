var smap ;


	
function startOrder(orders_items){
	if($("#client_name")){ 
		if($("#client_name").val() && $("#client_name").val().length == 0 ){
			alert("Please enter a Client Name");
			return;
		}
	}
	
	$("#orders_items").val(orders_items);
	document.forms[0].submit();
}

function startSubscriptionOrder(id){
	$("#id").val(id);
	document.forms[0].submit();
}

function createRadioButtons(){
	//Get Exising Select Options    
	$('select#id').each(function(i, select){
	    var $select = $(select);
	  
	    $select.find('option').each(function(j, option){
	    	  
	    	var $option = $(option);
	        // Create a radio:
	        var $radio = $('<input type="radio" id="id" />');
	        // Set name and value:
	        $radio.attr('name', $select.attr('name')).attr('value', $option.val());
	        // Set checked if the option was selected
	        //if ($option.attr('selected') || j == 0) $radio.attr('checked', 'checked');
	        // Insert radio before select box:
	        $select.before($radio);
	        // Insert a label:
	        $select.before(
	          $("<label />").attr('for', $select.attr('name')).text("  " + $option.text())
	        );
	        // Insert a <br />:
	        $select.before("<br/>");
	    });
	    $select.remove();
	});	
}

function createRadioClassButtons(){
	//Get Exising Select Options    
	$('.do_radio').each(function(i, select){
	    var $select = $(select);
	  
	    $select.find('option').each(function(j, option){
	    	  
	    	var $option = $(option);
	        // Create a radio:
	        var $radio = $('<input type="radio" id="id" />');
	        // Set name and value:
	        $radio.attr('name', $select.attr('name')).attr('value', $option.val());
	        // Set checked if the option was selected
	        if ($option.attr('selected') || j == 0) $radio.attr('checked', 'checked');
	        // Insert radio before select box:
	        $select.before($radio);
	        // Insert a label:
	        $select.before(
	          $("<label />").attr('for', $select.attr('name')).text("  " + $option.text())
	        );
	        // Insert a <br />:
	        $select.before("<br/>");
	    });
	    $select.remove();
	});	
}
function replaceAll(find, replace, str) {
  return str.replace(new RegExp(find, 'g'), replace);
}

function toggleCheckbox(className){ 
	var allItems = $("." + className);
	if(allItems && allItems.length > 0){
		var toState = allItems[0].checked? false: true;
		for(var i = 0; i < allItems.length; i++){
			allItems[i].checked = toState;
		}
	}
}

function hideSocial(){
	$('#share_job_social').tooltip('hide');
} 
 
function initializeHome(){
	 
}

var careerCompanyList ;
var multiSelectDropdowns;

function initializeRevenueSearch(){
	jQuery("#start_date").combodate({minYear: 2015, maxYear: 2035, yearDescending: false});
	jQuery("#end_date").combodate({minYear: 2015, maxYear: 2035, yearDescending: false});
	
}

function initializeEmails(){
	var allOptions = jQuery(".multipleitems");
	 
	for(var i = 0; i < allOptions.length; i++){
		if( allOptions[i].id.length > 0 ){
			if(jQuery("#" + allOptions[i].id)){
				jQuery("#" + allOptions[i].id).multiselect();
			}
		}
	}
}

function initializeContent(){
	
	var allOptions = jQuery(".multipleitems");
	for(var i = 0; i < allOptions.length; i++){
		if( allOptions[i].id.length > 0 ){
			if(jQuery("#" + allOptions[i].id)){
				jQuery("#" + allOptions[i].id).multiselect();
			}
		}
	}	
}
  

function emailJob(){
	 
	$("#emailJob").dialog({
			position: { my: "left top", at: "left bottom", of: jQuery("#emailJobButton") }
	});
	
}

 

function toggle(theID, theLink){
	if($("#" + theID).css("display") == "none"){
		$("#" + theID).css("display", "block");
	}else {
		$("#" + theID).css("display", "none");
	}
}


 
function toggleSearch(){
	$('.toggle_search').toggle('slow');
}