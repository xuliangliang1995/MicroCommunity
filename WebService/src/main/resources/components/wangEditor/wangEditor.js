(function(vc){
    vc.extends({
        data: {
            editor: null,
            /*editorContent: null,*/
        },
        propTypes: {
            parentComponent: vc.propTypes.string,
        },
        _initMethod() {
            vc.component.initWangEditor();
        },
        _initEvent() {
            vc.on('wangEditor', 'setWangEditorContent', vc.component.setWangEditorContent)
        },
        methods: {
            /**
             * 初始化wangEditor编辑器
             * */
            initWangEditor() {
                var E = window.wangEditor;
                vc.component.editor = new E('#wangEditor');
                vc.component.editor.customConfig.uploadImgShowBase64 = true;
                vc.component.editor.customConfig.menus = [
                    'head',  // 标题
                    'bold',  // 粗体
                    'fontSize',  // 字号
                    'italic',  // 斜体
                    'underline',  // 下划线
                    'strikeThrough',  // 删除线
                    'foreColor',  // 文字颜色
                    'backColor',  // 背景颜色
                    'link',  // 插入链接
                    'list',  // 列表
                    'justify',  // 对齐方式
                    'quote',  // 引用
                    'emoticon',  // 表情
                    'image',  // 插入图片
                    'table',  // 表格
                ];

                vc.component.editor.customConfig.onchange = (html) => {
                    /*vc.component.editorContent = html;*/
                    vc.emit($props.parentComponent, 'changeEditor', html);
                }
                vc.component.editor.create();
            },
            /**
             * 改变富文本的编辑状态
             * @param {Boolean} status ture:可编辑   false: 禁止编辑
             * */
            changeEditorStatus(status) {
                vc.component.editor.$textElem.attr('contenteditable', status);
            },
            getWangEditorContent() {
                vc.component.editor.txt.html();
            },
            /**
             * 设置wangEditor编辑器的内容
             * @param {String} content 编辑的内容
             * */
            setWangEditorContent(content) {
                /*vc.component.editorContent = content;*/
                vc.component.editor.txt.html(content);
            },
            /* 清空编辑器中的内容 */
            clearWangEditorContent() {
                vc.component.editor.txt.clear();
            }
        }
    })
})(window.vc)