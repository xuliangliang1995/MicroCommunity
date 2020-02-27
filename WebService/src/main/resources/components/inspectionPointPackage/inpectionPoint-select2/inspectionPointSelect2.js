(function (vc) {
    vc.extends({
        propTypes: {
            parentModal: vc.propTypes.string,
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            inspectionPointSelect2Info: {
                inspectionPoints: [],
                inspectionId: '-1',
                inspectionName: '',
                inspectionPointSelector: {},
            }
        },
        watch: {
            inspectionPointSelect2Info: {
                deep: true,
                handler: function () {
                    vc.emit($props.callBackListener, $props.callBackFunction, this.inspectionPointSelect2Info);
                }
            }
        },
        _initMethod: function () {
            this._initinspectionPointSelect2();
        },
        _initEvent: function () {
            //监听 modal 打开
            vc.on('inspectionPointSelect2', 'setInspectionPoints', function (_param) {
                vc.copyObject(_param, this.inspectionPointSelect2Info);

                var option = new Option(_param.inspectionName, _param.inspectionId, true, true);
                this.inspectionPointSelect2Info.inspectionPointSelector.append(option);
            });

            vc.on('inspectionPointSelect2', 'clearInspectionPoints', function (_param) {
                this.inspectionPointSelect2Info = {
                    inspectionPoints: [],
                    inspectionId: '-1',
                    inspectionName: '',
                    inspectionPointSelector: {},
                };
            });
        },
        methods: {
            _initinspectionPointSelect2: function () {
                console.log("调用_initinspectionPointSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {
                };
                $.fn.select2.defaults.set('width', '100%');
                this.inspectionPointSelect2Info.inspectionPointSelector = $('#inspectionPointSelector').select2({
                    placeholder: '必填，请选择巡检点',
                    allowClear: true,//允许清空
                    escapeMarkup: function (markup) {
                        return markup;
                    }, // 自定义格式化防止xss注入
                    ajax: {
                        url: "/callComponent/inspectionPointSelect2/list",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if (params.hasOwnProperty("term")) {
                                _term = params.term;
                            }
                            return {
                                inspectionName: _term,
                                page: 1,
                                row: 10,
                                communityId: vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, this._filterInspectionPointData(data.inspectionPoints));
                            return {
                                results: this._filterInspectionPointData(data.inspectionPoints)
                            };
                        },
                        cache: true
                    }
                });
                $('#inspectionPointSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select', evt);
                    this.inspectionPointSelect2Info.inspectionId = evt.params.data.id;
                    this.inspectionPointSelect2Info.inspectionName = evt.params.data.text;
                });

                $('#inspectionPointSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect', evt);
                    this.inspectionPointSelect2Info.inspectionId = '-1';
                    this.inspectionPointSelect2Info.inspectionName = '';

                });
            },
            _filterInspectionPointData: function (_inspectionPoints) {
                var _tmpInspectionPoints = [];
                for (var i = 0; i < _inspectionPoints.length; i++) {
                    var _tmpInspectionPoint = {
                        id: _inspectionPoints[i].inspectionId,
                        text: _inspectionPoints[i].inspectionName
                    };
                    _tmpInspectionPoints.push(_tmpInspectionPoint);
                }
                return _tmpInspectionPoints;
            }
        }
    });
})(window.vc);
