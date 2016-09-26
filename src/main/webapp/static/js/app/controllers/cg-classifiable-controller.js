function CgClassifiableController(ctrl,classificationService){

    ctrl.classifications = [];

    ctrl.onClassificationSelected = function(){
        classificationService.onClassificationSelected(ctrl.classifications,ctrl.model.classification);
    };

    ctrl.getClassifications = function(){
        ctrl.classifications = classificationService.getAllForProduct();
    };

    ctrl.getClassifications();

};