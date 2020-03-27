(function(vc){
    vc.extends({
        data:{
            hasOriginalPrice: false,
            commodityInfo: {
                originalPrice: '',
                currentPrice: '',
                remark: '',
                title: '',
                commodityId: -1,
                communityId: JSON.parse(localStorage.getItem('hc_currentCommunityInfo')).communityId,
                show: 1,    // 1为上架 0下架
                commodityPhotos: [],
                intro: '',
                stockpile: '',
            }
        },
        _initMethod() {
            this.Vue.config.devtools = true;
        },
        _initEvent() {
            vc.on('editCommodityModal', 'changeEditor', vc.component.changeEditor);
            vc.on('editCommodityModal', 'show', vc.component.listenerShowModal);
        },
        methods: {
            editorCommodityValidate () {
                return vc.validate.validate({
                    commodityInfo: vc.component.commodityInfo
                }, {
                    'commodityInfo.currentPrice': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "价格不能为空"
                        },
                    ],
                    'commodityInfo.title': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "标题不能为空"
                        },
                    ],
                    'commodityInfo.intro': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "商品介绍不能为空"
                        },
                    ],
                    'commodityInfo.stockpile': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "库存不能为空"
                        },
                    ]
                })
            },
            /**
             * 监听富文本编辑器内容修改
             * @param {String} html 富文本修改后的内容
             * */
            changeEditor(html) {
                vc.component.commodityInfo.intro = html;
            },
            /**
             * 监听弹窗显示
             * @param {Number} id  如果id不为0说明是编辑商品
             * */
            listenerShowModal(id) {
                $('#editCommodityModal').modal('show');
                vc.component.resetCommodityInfo();
            },
            /** 重置商品信息数据  */
            resetCommodityInfo() {
                vc.component.commodityInfo = {
                    originalPrice: '',
                    currentPrice: '',
                    remark: '',
                    title: '',
                    commodityId: -1,
                    communityId: JSON.parse(localStorage.getItem('hc_currentCommunityInfo')).communityId,
                    show: 1,    // 1为上架 0下架
                    commodityPhotos: [],
                    intro: '',
                    stockpile: '',
                };
                vc.emit('wangEditor', 'setWangEditorContent', vc.component.commodityInfo.intro);
            },
            /* 触发选择本地图片操作 */
            emitChoosePhoto() {
                $("#uploadOwnerPhoto").trigger("click")
            },
            /**
             * 选择图片
             * @param {Object} event 触发事件的对象数据
             * */
            choosePhoto(event) {
                var photoFiles = event.target.files;
                if (photoFiles && photoFiles.length > 0) {
                    // 获取目前上传的文件
                    var file = photoFiles[0];// 文件大小校验的动作
                    if (file.size > 1024 * 1024 * 1) {
                        vc.toast("图片大小不能超过 2MB!")
                        return false;
                    }
                    var reader = new FileReader(); //新建FileReader对象
                    reader.readAsDataURL(file); //读取为base64
                    reader.onloadend = async function (e) {
                        let imgSrc = await vc.component.uploadPhoto(reader.result).catch(err => {
                            console.error('上传图片错误', err)
                        });
                        vc.component.commodityInfo.commodityPhotos.push(imgSrc);
                    }
                }
            },
            /**
             * 上传图片
             * @param {String} base64
             * @return Promise<String>
             * */
            async uploadPhoto(base64) {
                return new Promise((reslove, reject) => {
                    vc.http.post('uploadImg', 'upload', {
                        img: base64,
                    },
                    {
                        headres: {
                            "Content-Type": "application/json"
                        }
                    },
                    (resStr, res) => {
                        reslove(res.body.fileSaveName);
                    },
                    (errText, err) => {
                        reject(errText);
                    })
                })
            },
            /**
             * 删除商品图片
             * @param {Number} index 要删除的元素的下标
             * */
            removeCommodityPhotos(index) {
                vc.component.commodityInfo.commodityPhotos.splice(index, 1);
            },
            /* 创建商品还是修改商品 */
            createOrChange() {
                if (!vc.component.editorCommodityValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }
                if (vc.component.commodityInfo.commodityId > -1) {
                    vc.component.changeCommodityInfo();
                } else {
                    vc.component.createCommodityInfo();
                }
            },
            /** 修改商品信息  */
            changeCommodityInfo() {

            },
            /**
             * 处理价格
             * @Param {Object} commodityInfo 商品信息
             * */
            priceDispose(commodityInfo) {
                commodityInfo.currentPrice = commodityInfo.currentPrice*100;
                if (!vc.component.hasOriginalPrice) {
                    commodityInfo.originalPrice = commodityInfo.currentPrice;
                } else {
                    commodityInfo.originalPrice = commodityInfo.originalPrice*100;
                }
            },
            /** 上传商品信息*/
            createCommodityInfo() {
                let commodityInfo = {};
                for (let key in vc.component.commodityInfo) {
                    if (key == 'commodityPhotos') {
                        commodityInfo[key] = vc.component.commodityInfo[key].toString();
                    } else {
                        commodityInfo[key] = vc.component.commodityInfo[key];
                    }
                }
                vc.component.priceDispose(commodityInfo);
                console.log('修改后的commodity中的价格', commodityInfo);
                vc.http.post('addCommodity',
                    'save',
                    commodityInfo,
                    {
                        headres: {
                            "Content-Type": "application/json"
                        }
                    },
                    (resText, res) => {
                        console.log(resText, '上传商品信息成功返回', res);
                        $('#editCommodityModal').modal('hide');
                        vc.component.resetCommodityInfo();
                    },
                    (errText, err) => {
                        console.log(errText, '上传商品信息失败返回', err)
                    })
            }
        }
    });
})(window.vc);