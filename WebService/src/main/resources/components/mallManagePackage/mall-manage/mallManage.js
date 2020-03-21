


(function(vc) {
    const shelvesTab = {
        SHELVES_ON_TAB: 1,
        SHELVES_OFF_TAB: 2,
    },
    orderAndMallTab = {
        ORDER_TAB: 1,
        MALL_TAB: 2,
    };
    vc.extends({
        data: {
            orderAndMallTab: orderAndMallTab.ORDER_TAB,
            shelvesTab: shelvesTab.SHELVES_ON_TAB,
        },
        _initMethod() {
            this.Vue.config.devtools = true;
        },
        _initEvent() {

        },
        methods: {
            /**
             * 显示编辑商品弹窗
             * @param {Number} id 商品的id
             * */
            showEditCommodityModal(id = 0) {
                if (typeof id != 'number') {
                    id = 0;
                }
                vc.emit('editCommodityModal', 'show', id);
            }
        },
    })
})(window.vc);