


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
            console.log(this, '神奇的封装', vc.component.tabStatus);
            this.Vue.config.devtools = true;
        },
        _initEvent() {

        },
        methods: {

        },
    })
})(window.vc);