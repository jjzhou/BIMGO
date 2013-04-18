var labelType, useGradients, nativeTextSupport, animate;

(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport 
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
  nativeTextSupport = labelType == 'Native';
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
  elem: false,
  write: function(text){
    if (!this.elem) 
      this.elem = document.getElementById('log');
    this.elem.innerHTML = text;
    this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
  }
};


function init(){
  //init data
  var json = {
    "children": [
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#E0F832",
                        "image": "http://userserve-ak.last.fm/serve/300x300/11403219.jpg",
                        "$area": 250
                    },
                    "id": "chenggangTong",
                    "name": "Tong, Chenggang"
                }
            ],
            "data": {
                "$area": 120
            },
            "id": "BIMHead",
            "name": "BIM Head"
        },
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32579429.jpg",
                        "$area": 300
                    },
                    "id": "hehuang",
                    "name": "Huang, He"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/5264426.jpg",
                        "$area": 185
                    },
                    "id": "yongjiangZhao",
                    "name": "Zhao, Yongjiang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/5264426.jpg",
                        "$area": 185
                    },
                    "id": "liyuChen",
                    "name": "Chen, Liyu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://cdn.last.fm/flatness/catalogue/noimage/2/default_album_medium.png",
                        "$area": 185
                    },
                    "id": "yuchen",
                    "name": "Chen, Yu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/16799743.jpg",
                        "$area": 185
                    },
                    "id": "chaoSun",
                    "name": "Sun, Chao"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://cdn.last.fm/flatness/catalogue/noimage/2/default_album_medium.png",
                        "$area": 185
                    },
                    "id": "tingtingHe",
                    "name": "He, Tingting"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32595059.jpg",
                        "$area": 185
                    },
                    "id": "xinHuang",
                    "name": "Huang, Xin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/29274729.jpg",
                        "$area": 185
                    },
                    "id": "hongfeiWang",
                    "name": "Wang, Hongfei"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8605651.jpg",
                        "$area": 185
                    },
                    "id": "lingliangXie",
                    "name": "Xie, lingliang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/9658733.jpg",
                        "$area": 185
                    },
                    "id": "guohuangZhang",
                    "name": "Zhang, Guohuang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/26053425.jpg",
                        "$area": 185
                    },
                    "id": "leiDu",
                    "name": "Du, Lei"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/30352203.jpg",
                        "$area": 185
                    },
                    "id": "xiaohuaZhang",
                    "name": "Zhang, Xiaohua"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32457599.jpg",
                        "$area": 185
                    },
                    "id": "jianminFeng",
                    "name": "Feng, Jianmin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8776205.jpg",
                        "$area": 185
                    },
                    "id": "jianminLian",
                    "name": "Lian, Jianmin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/15858421.jpg",
                        "$area": 185
                    },
                    "id": "yongjiangZhao",
                    "name": "Zhao, Yongjiang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32070871.jpg",
                        "$area": 185
                    },
                    "id": "xiaoqinLin",
                    "name": "Lin, Xiaoqin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8591345.jpg",
                        "$area": 185
                    },
                    "id": "siyuanLiu",
                    "name": "Liu, Siyuan"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8634627.jpg",
                        "$area": 185
                    },
                    "id": "shuboMa",
                    "name": "Ma, Shubo"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/25402479.jpg",
                        "$area": 185
                    },
                    "id": "xuhuaMo",
                    "name": "Mo, Xuhua"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://cdn.last.fm/flatness/catalogue/noimage/2/default_album_medium.png",
                        "$area": 185
                    },
                    "id": "weiWang",
                    "name": "Wang, Wei"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8480501.jpg",
                        "$area": 185
                    },
                    "id": "xinleiWang",
                    "name": "Wang, Xinlei"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://images.amazon.com/images/P/B0000296JW.01.MZZZZZZZ.jpg",
                        "$area": 185
                    },
                    "id": "linYang",
                    "name": "Yang, Lin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://images.amazon.com/images/P/B0009HLDFU.01.MZZZZZZZ.jpg",
                        "$area": 185
                    },
                    "id": "conghuiZhang",
                    "name": "Zhang, Conghui"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#B74732",
                        "image": "http://userserve-ak.last.fm/serve/300x300/26497553.jpg",
                        "$area": 185
                    },
                    "id": "qunyuZhang",
                    "name": "Zhang, Qunyu"
                }
            ],
            "data": {
                "$area": 500
            },
            "id": "BI",
            "name": "BI"
        },
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#946A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32579429.jpg",
                        "$area": 60
                    },
                    "id": "weitaoLiu",
                    "name": "Liu, Weitao"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#946A32",
                        "image": "http://images.amazon.com/images/P/B0009HLDFU.01.MZZZZZZZ.jpg",
                        "$area": 60
                    },
                    "id": "wenzhaiChen",
                    "name": "Chen, Wenzhai"
                }
            ],
            "data": {
                "$area": 120
            },
            "id": "EUC-SI",
            "name": "EUC-SI"
        },
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8673371.jpg",
                        "$area": 250
                    },
                    "id": "xiangFang",
                    "name": "Fang, Xiang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8590493.jpg",
                        "$area": 181
                    },
                    "id": "zhuya",
                    "name": "Zhu, Ya"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/17597653.jpg",
                        "$area": 181
                    },
                    "id": "yinwenliang",
                    "name": "Yin, WenLiang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/15231979.jpg",
                        "$area": 181
                    },
                    "id": "xiongjiaxin",
                    "name": "Xiong, Jiaxin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://images.amazon.com/images/P/B0007LCNNE.01.MZZZZZZZ.jpg",
                        "$area": 181
                    },
                    "id": "wangyuanhui",
                    "name": "Wang, Yuanhui"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/7862623.png",
                        "$area": 181
                    },
                    "id": "qianyongheng",
                    "name": "Qian, Yongheng"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8600371.jpg",
                        "$area": 181
                    },
                    "id": "mazengkai",
                    "name": "Ma, Zengkai"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8590515.jpg",
                        "$area": 181
                    },
                    "id": "fangjin",
                    "name": "Fang, Jin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/15113951.jpg",
                        "$area": 181
                    },
                    "id": "chenhongjun",
                    "name": "Chen, Hongjun"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/45729417.jpg",
                        "$area": 181
                    },
                    "id": "yanghui",
                    "name": "Yang, Hui"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://images.amazon.com/images/P/B00005V5PW.01.MZZZZZZZ.jpg",
                        "$area": 181
                    },
                    "id": "wangtong",
                    "name": "Wang, Tong"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/15157989.jpg",
                        "$area": 181
                    },
                    "id": "wanggang",
                    "name": "Wang, Gang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/17594883.jpg",
                        "$area": 181
                    },
                    "id": "wangdaoshu",
                    "name": "Wang, Daoshu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/31550385.jpg",
                        "$area": 181
                    },
                    "id": "shijianhua",
                    "name": "Shi, Jianhua"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32309285.jpg",
                        "$area": 181
                    },
                    "id": "maqinghui",
                    "name": "Ma, Qinhui"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8599099.jpg",
                        "$area": 181
                    },
                    "id": "hutao",
                    "name": "Hu, Tao"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/30082075.jpg",
                        "$area": 181
                    },
                    "id": "hukai",
                    "name": "Hu, Kai"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "guojiansong",
                    "name": "Guo, Jiansong"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8673371.jpg",
                        "$area": 181
                    },
                    "id": "gongyu",
                    "name": "Gong, Yu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "zhaoxiyu",
                    "name": "Zhao, Xiyu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8673371.jpg",
                        "$area": 181
                    },
                    "id": "shitingting",
                    "name": "Shi, TingTing"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#6C9299",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "huangjiaxin",
                    "name": "Huang, JiaXin"
                }
            ],
            "data": {
                "$area": 380
            },
            "id": "SOA&BPM",
            "name": "SOA&BPM"
        },
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://images.amazon.com/images/P/B000002IU3.01.LZZZZZZZ.jpg",
                        "$area": 300
                    },
                    "id": "longchunLi",
                    "name": "Li, Longchun"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8599099.jpg",
                        "$area": 181
                    },
                    "id": "liyankun",
                    "name": "Li, YanKun"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/30082075.jpg",
                        "$area": 181
                    },
                    "id": "houlimin",
                    "name": "Hou, LiMin"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "huolang",
                    "name": "Huo, Lang"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8673371.jpg",
                        "$area": 181
                    },
                    "id": "lihui",
                    "name": "Li, Hui"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "wangjun",
                    "name": "Wang, Jun"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8673371.jpg",
                        "$area": 181
                    },
                    "id": "zhanglongfei",
                    "name": "Zhang, LongFei"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#5DA132",
                        "image": "http://userserve-ak.last.fm/serve/300x300/21881921.jpg",
                        "$area": 181
                    },
                    "id": "pengshuhui",
                    "name": "Peng, Shuhui"
                }
            ],
            "data": {
                "$area": 280
            },
            "id": "ITSP",
            "name": "ITSP"
        },
        {
            "children": [
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/11403219.jpg",
                        "$area": 250
                    },
                    "id": "cloudZhang",
                    "name": "Zhang, Cloud"
                },
                {
                "children": [],
                "data": {
                    "$color": "#847A32",
                    "image": "http://userserve-ak.last.fm/serve/300x300/32579429.jpg",
                    "$area": 185
                },
                "id": "chenjie",
                "name": "Chen, Jie"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/5264426.jpg",
                        "$area": 185
                    },
                    "id": "guoqi",
                    "name": "Guo, Qi"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/5264426.jpg",
                        "$area": 185
                    },
                    "id": "licongcong",
                    "name": "Li, Congcong"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://cdn.last.fm/flatness/catalogue/noimage/2/default_album_medium.png",
                        "$area": 185
                    },
                    "id": "liuchenyan",
                    "name": "Liu, Chenyan"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32349839.jpg",
                        "$area": 185
                    },
                    "id": "shuZhou",
                    "name": "Zhou, Shu"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/38753425.jpg",
                        "$area": 185
                    },
                    "id": "chuanxiAn",
                    "name": "An, Chuanxi"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/16799743.jpg",
                        "$area": 185
                    },
                    "id": "longnan",
                    "name": "Long, Nan"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://cdn.last.fm/flatness/catalogue/noimage/2/default_album_medium.png",
                        "$area": 185
                    },
                    "id": "qinchong",
                    "name": "Qin, Chong"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32595059.jpg",
                        "$area": 185
                    },
                    "id": "wangjing",
                    "name": "Wang, Jing"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/29274729.jpg",
                        "$area": 185
                    },
                    "id": "wuzhenfeng",
                    "name": "Wu, Zhenfeng"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8605651.jpg",
                        "$area": 185
                    },
                    "id": "yuhuashan",
                    "name": "Yu, Huashan"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/9658733.jpg",
                        "$area": 185
                    },
                    "id": "zhangfeng",
                    "name": "Zhang, Feng"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/26053425.jpg",
                        "$area": 185
                    },
                    "id": "zhoujeffrey",
                    "name": "Zhou, Jian"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/30352203.jpg",
                        "$area": 185
                    },
                    "id": "zhouzhen",
                    "name": "Zhou, Zhen"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/32457599.jpg",
                        "$area": 185
                    },
                    "id": "zhuyunyun",
                    "name": "Zhu, yunyun"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/8776205.jpg",
                        "$area": 185
                    },
                    "id": "wangbo",
                    "name": "Wang, Bo"
                },
                {
                    "children": [],
                    "data": {
                        "$color": "#847A32",
                        "image": "http://userserve-ak.last.fm/serve/300x300/11393921.jpg",
                        "$area": 185
                    },
                    "id": "yanluLiu",
                    "name": "Liu, Yanlu"
                }
            ],
            "data": {
                "$area": 380
            },
            "id": "HSE",
            "name": "HSE"
        }
   ], 
   "data": {}, 
   "id": "root", 
   "name": "Business Information Management"
   };
  //end
  //init TreeMap
  var tm = new $jit.TM.Squarified({
    //where to inject the visualization
    injectInto: 'infovis',
    //parent box title heights
    titleHeight: 15,
    //enable animations
    animate: animate,
    //box offsets
    offset: 1,
    //Attach left and right click events
    Events: {
      enable: true,
      onClick: function(node) {
        if(node) tm.enter(node);
      },
      onRightClick: function() {
        tm.out();
      }
    },
    duration: 1000,
    //Enable tips
    Tips: {
      enable: true,
      //add positioning offsets
      offsetX: 20,
      offsetY: 20,
      //implement the onShow method to
      //add content to the tooltip when a node
      //is hovered
      onShow: function(tip, node, isLeaf, domElement) {
        var html = "<div class=\"tip-title\">" + node.name 
          + "</div><div class=\"tip-text\">";
        var data = node.data;
        if(data.playcount) {
          html += "play count: " + data.playcount;
        }
        if(data.image) {
          html += "<img src=\""+ data.image +"\" class=\"album\" />";
        }
        tip.innerHTML =  html; 
      }  
    },
    //Add the name of the node in the correponding label
    //This method is called once, on label creation.
    onCreateLabel: function(domElement, node){
        domElement.innerHTML = node.name;
        var style = domElement.style;
        style.display = '';
        style.border = '1px solid transparent';
        domElement.onmouseover = function() {
          style.border = '1px solid #9FD4FF';
        };
        domElement.onmouseout = function() {
          style.border = '1px solid transparent';
        };
    }
  });
  tm.loadJSON(json);
  tm.refresh();
  //end
  //add events to radio buttons
  var sq = $jit.id('r-sq'),
      st = $jit.id('r-st'),
      sd = $jit.id('r-sd');
  var util = $jit.util;
  util.addEvent(sq, 'change', function() {
    if(!sq.checked) return;
    util.extend(tm, new $jit.Layouts.TM.Squarified);
    tm.refresh();
  });
  util.addEvent(st, 'change', function() {
    if(!st.checked) return;
    util.extend(tm, new $jit.Layouts.TM.Strip);
    tm.layout.orientation = "v";
    tm.refresh();
  });
  util.addEvent(sd, 'change', function() {
    if(!sd.checked) return;
    util.extend(tm, new $jit.Layouts.TM.SliceAndDice);
    tm.layout.orientation = "v";
    tm.refresh();
  });
  //add event to the back button
  var back = $jit.id('back');
  $jit.util.addEvent(back, 'click', function() {
    tm.out();
  });
}
