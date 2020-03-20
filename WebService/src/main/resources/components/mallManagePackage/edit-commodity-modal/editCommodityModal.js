(function(vc){
    vc.extends({
        data:{

        },
        _initMethod() {

        },
        _initEvent() {
            vc.on('editCommodityModal', 'changeEditor', vc.component.changeEditor);
            vc.on('editCommodityModal', 'show', vc.component.listenerShowModal);
        },
        methods: {
            /**
             * 监听富文本编辑器内容修改
             * @param {String} html 富文本修改后的内容
             * */
            changeEditor(html) {
                console.log('修改编辑器内容===>', html);
            },
            /**
             * 监听弹窗显示
             * @param {Number} id  如果id不为0说明是编辑商品
             * */
            listenerShowModal(id) {
                $('#editCommodityModal').modal('show');
            }
        }
    });
})(window.vc);