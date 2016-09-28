function CgModelController(ctrl,modelService,tabService,tabName,tabsId,factory,$scope){

    ctrl.model = {};
    ctrl.modelForm = {};

    ctrl.saveModel = function(successCall){
        var callback = function(resp){
            if(successCall!=null){
                successCall(resp);
            }
            var args = {open : true, id: resp.id, name: resp[tabName], tabsId:tabsId, factory:factory};
            tabService.addTab(args,$scope);
        };

        var saved = modelService.save(ctrl.modelForm,ctrl.model,callback);

        if(ctrl.model.id == null && saved== true){
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