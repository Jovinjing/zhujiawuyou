package com.example.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author YuePeng
 * @date 2022/12/28 15:12
 * 这里只是模拟部分字段，并非真实表结构
 */
@Data
public class DataForm implements Serializable {
    private static final long serialVersionUID = -5652177874505967327L;

    private String provinceName;
    // 请假人
    private List ageData;
    // 请假原因
    private List areaData;
    private List genderData;
    private List seasonData;
    private List budgetData;
    // 请假时间

}