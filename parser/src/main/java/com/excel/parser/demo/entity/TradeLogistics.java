package com.excel.parser.demo.entity;

import com.excel.parser.annotation.Display;
import com.excel.parser.annotation.EnableMapping;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EnableMapping
@EqualsAndHashCode
public class TradeLogistics {

    private Long id;

    @Display("交货单号")
    @EqualsAndHashCode.Include
    private String VBELN;//交货单号

    @Display("销售订单")
    @EqualsAndHashCode.Include
    private String LIFEX;//外部单号

    @Display("销售订单")
    @EqualsAndHashCode.Include
    private String BSTKD;//采购订单号

    @Display("交货单创建日期")
    @EqualsAndHashCode.Include
    private Date WADAT_IST;//交货单过账日期

    private String WERKS;//发货工厂,发货仓库  中信ERP发货的仓库编号

    private String NAME1;//发货工厂名称

    @Display(constValue = "1")
    private Byte ZFX;//出入库标识 1、采购出库单 2、退货入库单

    private String ZZLIFNR;//物流供应商

    private String NAME2;//供应商名称

    private String ZSHTYP;//装运类型

    private String BOLNR;//提单、物流单号

    private Date ZCKRQ;//出库日期

    private Date ZFYRQ;//发运日期

    private Date create_time;

    private String create_uid;

    private Date update_time;

    private String update_uid;

    private Byte is_deleted;

    private Byte convert_status;

    private String convert_message;

    private String ext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVBELN() {
        return VBELN;
    }

    public void setVBELN(String VBELN) {
        this.VBELN = VBELN == null ? null : VBELN.trim();
    }

    public String getLIFEX() {
        return LIFEX;
    }

    public void setLIFEX(String LIFEX) {
        this.LIFEX = LIFEX == null ? null : LIFEX.trim();
    }

    public String getBSTKD() {
        return BSTKD;
    }

    public void setBSTKD(String BSTKD) {
        this.BSTKD = BSTKD == null ? null : BSTKD.trim();
    }

    public Date getWADAT_IST() {
        return WADAT_IST;
    }

    public void setWADAT_IST(Date WADAT_IST) {
        this.WADAT_IST = WADAT_IST;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS == null ? null : WERKS.trim();
    }

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String NAME1) {
        this.NAME1 = NAME1 == null ? null : NAME1.trim();
    }

    public Byte getZFX() {
        return ZFX;
    }

    public void setZFX(Byte ZFX) {
        this.ZFX = ZFX;
    }

    public String getZZLIFNR() {
        return ZZLIFNR;
    }

    public void setZZLIFNR(String ZZLIFNR) {
        this.ZZLIFNR = ZZLIFNR == null ? null : ZZLIFNR.trim();
    }

    public String getNAME2() {
        return NAME2;
    }

    public void setNAME2(String NAME2) {
        this.NAME2 = NAME2 == null ? null : NAME2.trim();
    }

    public String getZSHTYP() {
        return ZSHTYP;
    }

    public void setZSHTYP(String ZSHTYP) {
        this.ZSHTYP = ZSHTYP == null ? null : ZSHTYP.trim();
    }

    public String getBOLNR() {
        return BOLNR;
    }

    public void setBOLNR(String BOLNR) {
        this.BOLNR = BOLNR == null ? null : BOLNR.trim();
    }

    public Date getZCKRQ() {
        return ZCKRQ;
    }

    public void setZCKRQ(Date ZCKRQ) {
        this.ZCKRQ = ZCKRQ;
    }

    public Date getZFYRQ() {
        return ZFYRQ;
    }

    public void setZFYRQ(Date ZFYRQ) {
        this.ZFYRQ = ZFYRQ;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCreate_uid() {
        return create_uid;
    }

    public void setCreate_uid(String create_uid) {
        this.create_uid = create_uid == null ? null : create_uid.trim();
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_uid() {
        return update_uid;
    }

    public void setUpdate_uid(String update_uid) {
        this.update_uid = update_uid == null ? null : update_uid.trim();
    }

    public Byte getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Byte is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Byte getConvert_status() {
        return convert_status;
    }

    public void setConvert_status(Byte convert_status) {
        this.convert_status = convert_status;
    }

    public String getConvert_message() {
        return convert_message;
    }

    public void setConvert_message(String convert_message) {
        this.convert_message = convert_message == null ? null : convert_message.trim();
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

}