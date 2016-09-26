function CgModelController(ctrl,modelService,tabService,tabName,tabsId,factory,$scope){

    ctrl.model = {};
    ctrl.modelForm = {};

    ctrl.saveModel = function(){
        var callback = function(resp){
            var args = {open : true, id: resp.id, name: resp[tabName], tabsId:tabsId, factory:factory};
            tabService.addTab(args,$scope);
        };

        if(modelService.save(ctrl.modelForm,ctrl.model,callback)==true){
            ctrl.model = modelService.getNew();
        };
    };

    ctrl.setModel = function(modelId){
        ctrl.model = modelService.getOne(modelId);
    };

    ctrl.newModel = function(){
        ctrl.model = modelService.getNew();
    };

};