package club.projectgaia.varys;


import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTest {
    @Test
    public void getHtml() throws Exception {
        //File in = new File("H:\\text.javbus");
        File in = new File("H:\\javbus_test.mp");
        Document doc = Jsoup.parse(in, "UTF-8");
        System.out.println(doc.outerHtml());
    }



}
