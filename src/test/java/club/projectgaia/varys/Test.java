package club.projectgaia.varys;


import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    @org.junit.Test
    public void test(){
        String test = "{\"status\":0,\"data\":{\"list\":[{\"DocID\":1122919298,\"Title\":\"精彩视频\",\"NodeId\":1136053,\"PubTime\":\"2018-05-31 16:55:48\",\"LinkUrl\":\"\",\"Abstract\":\\{ showColumnTit: 'yes', columnClass: '', columnTitClass: 'skinTit2', columnTitIsPic: '', columnTitBgPic: '', showColumnTitMore: '', columnPcMarginTop: '', columnMbMarginTop: '', columnPcMarginBottom: '', columnMbMarginBottom: '', columnPcPaddingTop: '', columnMbPaddingTop: '', columnPcPaddingBottom: '', columnMbPaddingBottom: '', columnTitPcH: '', columnTitMbH: '', SetCompose: [{ composeName: 'foucs-1', ComposeClass: '', advSkin: [''], composeCon: { composeConClass: '', topDistance: '20px', bottomDistance: '', advSkin: [''], modules: [{ dataId: '01', moduleTit: '', moduleTitLink: '', moduleSubTit: '', modulePcH: '', moduleMbH: '', MaxNum: 6, moduleClass: 'margin10B ElemlisB', advSkin: [''], animation: [], SetElem: { elemPcH: '', elemMbH: '', picPcH: '569px', picMbH: '223px', titPcH: '', titMbH: '', abstracPcH: '', abstracMbH: '', elemPcDistanceB: '', elemMbDistanceB: '', picPcDistanceB: '', picMbDistanceB: '', titPcDistanceB: '', titMbDistanceB: '', abstracPcDistanceB: '', abstracMbDistanceB: '', } }] } }],}\",\"keyword\":null,\"Editor\":null,\"Author\":\"刘梦姣\",\"IsLink\":1,\"SourceName\":null,\"PicLinks\":\"\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null}]},\"totalnum\":125}";
        System.out.println(test.replaceFirst("\"Abstract\":\\\\.*}\"",""));
        JSON.toJSON(test.replaceFirst("\"Abstract\":\\\\.*}\"",""));
    }

    @org.junit.Test
    public void t(){
    }

}
