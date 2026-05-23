package com.example.service.impl;

import com.deepoove.poi.XWPFTemplate;

import com.example.entity.DataForm;
import com.example.service.AiService;
import com.example.service.DataFormService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataFormServiceImpl implements DataFormService {
    @Resource
    private AiService aiService;

    @Override
    public byte[] downloadWord(DataForm dataForm) {
        String systemPrompt="你是一位严谨的租房app的商家，我会给你性别 年龄段 地区用户 季度人数 和预算金额的json数据\n" +
                "请你根据以上数据进行分析 并对以上的五个数据提分别提出关于年龄段，季度，性别，地域和房子的预算金额的五项项针对性和实际性的建议，不需要综合建议 然后请严格按照根据以下的格式进行输出：\n" +
                "         <省份>数据分析\n" +
                "性别分析：\n" +
                "性别建议：\n" +
                "地域分析：\n" +
                "地域建议：\n" +
                "年龄分析：\n" +
                "年龄建议：\n" +
                "季度分析：\n" +
                "季度建议：\n" +
                "预算金额分析：\n" +
                "预算金额建议：\n" +
                "\n";
        String userPrompt=dataForm.getAgeData()+"\n"+dataForm.getGenderData()+"\n"+dataForm.getAreaData()+"\n"+dataForm.getSeasonData()+"\n"+dataForm.getBudgetData()+"不要提出综合建议，不要有最后一句话的总结，不要出现任何其他的话语，比如注意什么的";
//        String userPrompt= "[{ name: \"儿童期\", value: 1 },{ name: \"青少年\", value: 1 }]\n" +
//                "[{ name: \"男\", value: 2 }，{ name: \"女\", value: 3 }]\n" +
//                "[{ name: '广东省', value: 2 },{ name: '四川省', value: 1 }]\n" +
//                "[1, 1, 1, 0]不要提出综合建议，不要有最后一句话的总结，不要出现任何其他的话语，比如注意什么的";\
        System.out.println(userPrompt);

        String text = aiService.doSyncRequest(systemPrompt,userPrompt,0.95f);
        // 提取性别分析
        Pattern genderPattern = Pattern.compile("性别分析：(.*?)性别建议：(.*?)地域分析：", Pattern.DOTALL);
        Matcher genderMatcher = genderPattern.matcher(text);
        String genderAnalysis = "";
        String genderAdvice = "";
        if (genderMatcher.find()) {
            genderAnalysis = genderMatcher.group(1).trim();
            genderAdvice = genderMatcher.group(2).trim();
        }

        // 提取地域分析
        Pattern areaPattern = Pattern.compile("地域分析：(.*?)地域建议：(.*?)年龄分析", Pattern.DOTALL);
        Matcher areaMatcher = areaPattern.matcher(text);
        String areaAnalysis = "";
        String areaAdvice = "";
        if (areaMatcher.find()) {
            areaAnalysis = areaMatcher.group(1).trim();
            areaAdvice = areaMatcher.group(2).trim();
        }

        // 提取年龄分析
        Pattern agePattern = Pattern.compile("年龄分析：(.*?)年龄建议：(.*?)季度分析", Pattern.DOTALL);
        Matcher ageMatcher = agePattern.matcher(text);
        String ageAnalysis = "";
        String ageAdvice = "";
        if (ageMatcher.find()) {
            ageAnalysis = ageMatcher.group(1).trim();
            ageAdvice = ageMatcher.group(2).trim();
        }

        // 提取季度分析
        Pattern seasonPattern = Pattern.compile("季度分析：(.*?)季度建议：(.*?)预算金额分析", Pattern.DOTALL);
        Matcher seasonMatcher = seasonPattern.matcher(text);
        String seasonAnalysis = "";
        String seasonAdvice = "";
        if (seasonMatcher.find()) {
            seasonAnalysis = seasonMatcher.group(1).trim();
            seasonAdvice = seasonMatcher.group(2).trim();
        }

        // 提取金额分析
        Pattern moneyPattern = Pattern.compile("预算金额分析：(.*?)预算金额建议：(.*)", Pattern.DOTALL);
        Matcher moneyMatcher = moneyPattern.matcher(text);
        String moneyAnalysis = "";
        String moneyAdvice = "";
        if (moneyMatcher.find()) {
            moneyAnalysis = moneyMatcher.group(1).trim();
            System.out.println("moneyAnalysis"+moneyAnalysis);
            moneyAdvice = moneyMatcher.group(2).trim();
            System.out.println("moneyAdvice"+moneyAdvice);

        }








        // 获取文件流
        InputStream stream = DataFormServiceImpl.class.getClassLoader().getResourceAsStream("template/downloadWord.docx");
        // 填充数据
        Map<String, String> data = new HashMap<>();
        data.put("provinceName", dataForm.getProvinceName());
        data.put("ageAnalysis", ageAnalysis);
        data.put("ageAdvice",ageAdvice);
        data.put("areaAnalysis",areaAnalysis);
        data.put("areaAdvice",areaAdvice);
        data.put("genderAnalysis",genderAnalysis);
        data.put("genderAdvice",genderAdvice);
        data.put("seasonAnalysis",seasonAnalysis);
        data.put("seasonAdvice",seasonAdvice);
        data.put("moneyAnalysis",moneyAnalysis);
        data.put("moneyAdvice",moneyAdvice);
        System.out.println(data);
        XWPFTemplate template = XWPFTemplate.compile(stream).render(data);
        //输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return getBytes(template, outputStream);
    }

    private byte[] getBytes(XWPFTemplate template, ByteArrayOutputStream os) {
        byte[] bytes = new byte[0];
        try {
            template.write(os);
            bytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                template.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}