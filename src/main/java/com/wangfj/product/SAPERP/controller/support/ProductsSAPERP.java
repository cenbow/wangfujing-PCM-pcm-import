package com.wangfj.product.SAPERP.controller.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsSAPERP {
	@JsonProperty(value = "GLFL")
	private String GLFL;// 管理分类（9位）
	@JsonProperty(value = "MTART")
	private String MTART;// 经营方式0经销，1代销，2联营，3平台服务，4租赁
	@JsonProperty(value = "MATKL")
	private String MATKL;// 商品类目(工业分类)
	@JsonProperty(value = "ATTYP")
	private String ATTYP;// 商品类别==========================!!!!!DELETE!!!!!
	@JsonProperty(value = "MATNR")
	private String MATNR;// 商品编码
	@JsonProperty(value = "S_MATNR")
	private String S_MATNR;// PCM专柜商品编码
	@JsonProperty(value = "MAKTX")
	private String MAKTX;// 商品描述（短文本）
	@JsonProperty(value = "LIFNR")
	private String LIFNR;// 供应商编码
	@JsonProperty(value = "ZGID")
	private String ZGID;// 专柜编码
	@JsonProperty(value = "ZZVDMAT")
	private String ZZVDMAT;// 供应商商品编码
	@JsonProperty(value = "ZZWLLX")
	private String ZZWLLX;// 物流类型（物流属性 1.Z001液体 2.Z002易碎 3.Z003液体与易碎 4.Z004粉末）
	@JsonProperty(value = "ZZBRAND_ID")
	private String ZZBRAND_ID;// 品牌编码(中台品牌编码)
	@JsonProperty(value = "ZZXXHC_FLAG")
	private String ZZXXHC_FLAG;// 先销后采(Y/N)
	@JsonProperty(value = "ZZDKNO")
	private String ZZDKNO;// 供应商货号 （SKU）
	@JsonProperty(value = "ZZPRICE")
	private String ZZPRICE;// 市场价
	@JsonProperty(value = "ZCOLOR")
	private String ZCOLOR;// 特性-颜色
	@JsonProperty(value = "ZZCOLORCODE")
	private String ZZCOLORCODE;// 颜色码
	@JsonProperty(value = "ZSIZE")
	private String ZSIZE;// 特性-尺码/规格
	@JsonProperty(value = "ZZSIZECODE")
	private String ZZSIZECODE;// 尺寸码
	@JsonProperty(value = "MEINS")
	private String MEINS;// 基本计量单位
	@JsonProperty(value = "ZZCOD")
	private String ZZCOD;// 可COD（Y/N）
	@JsonProperty(value = "ZZPACK")
	private String ZZPACK;// 可包装 (Y/N)
	@JsonProperty(value = "ZZCARD")
	private String ZZCARD;// 可贺卡 (Y/N)
	@JsonProperty(value = "SATNR")
	private String SATNR;// 商品母码==========================!!!!!DELETE!!!!!
	@JsonProperty(value = "MSTAV")
	private String MSTAV;// 跨分销链商品状态(停售标记)（Y/N）
	@JsonProperty(value = "ZLAND")
	private String ZLAND;// 原产国
	@JsonProperty(value = "ZLOCAL")
	private String ZLOCAL;// 原产地
	@JsonProperty(value = "ZLY_FLAG")
	private String ZLY_FLAG;// 虚库标志（Y/N）
	@JsonProperty(value = "ZZSSDATE")
	private String ZZSSDATE;// 上市日期（yyyymmdd）
	@JsonProperty(value = "GOODCLASS")
	private String GOODCLASS;// 商品款号
	@JsonProperty(value = "ZZGENDER")
	private String ZZGENDER;// 适用性别
	@JsonProperty(value = "TAXKM1")
	private String TAXKM1;// 销项税
	@JsonProperty(value = "TAXKM2")
	private String TAXKM2;// 消费税
	@JsonProperty(value = "TAXKM3")
	private String TAXKM3;// 进项税
	@JsonProperty(value = "ZZYCBZ")
	private String ZZYCBZ;// 是否有原厂包装
	@JsonProperty(value = "WERKS")
	private String WERKS;// 工厂（门店）
	@JsonProperty(value = "SAISO")
	private String SAISO;// 季节（01 春02夏03秋04冬05春夏06春秋07秋冬08四季09春夏秋）
	@JsonProperty(value = "ZZPICCODE")
	private String ZZPICCODE;// 照片编码
	@JsonProperty(value = "IS_GIFT")
	private String IS_GIFT;// 赠品范围
	@JsonProperty(value = "ZZVDBC")
	private String ZZVDBC;// 条码
	@JsonProperty(value = "ZSPRICE")
	private String ZSPRICE;// 售价
	@JsonProperty(value = "COLORSID")
	private String COLORSID;// 色系
	@JsonProperty(value = "UNIT")
	private String UNIT;// 销售单位
	@JsonProperty(value = "OFFERNUMBER")
	private String OFFERNUMBER;// 要约号
	@JsonProperty(value = "RATE_PRICE")
	private String RATE_PRICE;// 扣率
	@JsonProperty(value = "SCATE")
	private String SCATE;// 统计分类
	@JsonProperty(value = "ACTIONCODE")
	private String ACTIONCODE;// 本条记录对应的操作 (A添加；U更新；D删除)
	@JsonProperty(value = "ACTIONDATE")
	private String ACTIONDATE;// 操作时间
	@JsonProperty(value = "ACTIONPERSON")
	private String ACTIONPERSON;// 操作人
	@JsonProperty(value = "SUPPLIERPRODUCTCODE")
	private String SUPPLIERPRODUCTCODE;// 中台专柜商品编码

	public String getGLFL() {
		return GLFL;
	}

	public void setGLFL(String gLFL) {
		GLFL = gLFL;
	}

	public String getSUPPLIERPRODUCTCODE() {
		return SUPPLIERPRODUCTCODE;
	}

	public void setSUPPLIERPRODUCTCODE(String sUPPLIERPRODUCTCODE) {
		SUPPLIERPRODUCTCODE = sUPPLIERPRODUCTCODE;
	}

	public String getS_MATNR() {
		return S_MATNR;
	}

	public void setS_MATNR(String s_MATNR) {
		S_MATNR = s_MATNR;
	}

	public String getOFFERNUMBER() {
		return OFFERNUMBER;
	}

	public void setOFFERNUMBER(String oFFERNUMBER) {
		OFFERNUMBER = oFFERNUMBER;
	}

	public String getRATE_PRICE() {
		return RATE_PRICE;
	}

	public void setRATE_PRICE(String rATE_PRICE) {
		RATE_PRICE = rATE_PRICE;
	}

	public String getUNIT() {
		return UNIT;
	}

	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}

	public String getSCATE() {
		return SCATE;
	}

	public void setSCATE(String sCATE) {
		SCATE = sCATE;
	}

	public String getCOLORSID() {
		return COLORSID;
	}

	public void setCOLORSID(String cOLORSID) {
		COLORSID = cOLORSID;
	}

	public String getMTART() {
		return MTART;
	}

	public void setMTART(String mTART) {
		MTART = mTART;
	}

	public String getMATKL() {
		return MATKL;
	}

	public void setMATKL(String mATKL) {
		MATKL = mATKL;
	}

	public String getATTYP() {
		return ATTYP;
	}

	public void setATTYP(String aTTYP) {
		ATTYP = aTTYP;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getMAKTX() {
		return MAKTX;
	}

	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}

	public String getLIFNR() {
		return LIFNR;
	}

	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}

	public String getZGID() {
		return ZGID;
	}

	public void setZGID(String zGID) {
		ZGID = zGID;
	}

	public String getZZVDMAT() {
		return ZZVDMAT;
	}

	public void setZZVDMAT(String zZVDMAT) {
		ZZVDMAT = zZVDMAT;
	}

	public String getZZWLLX() {
		return ZZWLLX;
	}

	public void setZZWLLX(String zZWLLX) {
		ZZWLLX = zZWLLX;
	}

	public String getZZBRAND_ID() {
		return ZZBRAND_ID;
	}

	public void setZZBRAND_ID(String zZBRAND_ID) {
		ZZBRAND_ID = zZBRAND_ID;
	}

	public String getZZXXHC_FLAG() {
		return ZZXXHC_FLAG;
	}

	public void setZZXXHC_FLAG(String zZXXHC_FLAG) {
		ZZXXHC_FLAG = zZXXHC_FLAG;
	}

	public String getZZDKNO() {
		return ZZDKNO;
	}

	public void setZZDKNO(String zZDKNO) {
		ZZDKNO = zZDKNO;
	}

	public String getZZPRICE() {
		return ZZPRICE;
	}

	public void setZZPRICE(String zZPRICE) {
		ZZPRICE = zZPRICE;
	}

	public String getZCOLOR() {
		return ZCOLOR;
	}

	public void setZCOLOR(String zCOLOR) {
		ZCOLOR = zCOLOR;
	}

	public String getZZCOLORCODE() {
		return ZZCOLORCODE;
	}

	public void setZZCOLORCODE(String zZCOLORCODE) {
		ZZCOLORCODE = zZCOLORCODE;
	}

	public String getZSIZE() {
		return ZSIZE;
	}

	public void setZSIZE(String zSIZE) {
		ZSIZE = zSIZE;
	}

	public String getZZSIZECODE() {
		return ZZSIZECODE;
	}

	public void setZZSIZECODE(String zZSIZECODE) {
		ZZSIZECODE = zZSIZECODE;
	}

	public String getMEINS() {
		return MEINS;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public String getZZCOD() {
		return ZZCOD;
	}

	public void setZZCOD(String zZCOD) {
		ZZCOD = zZCOD;
	}

	public String getZZPACK() {
		return ZZPACK;
	}

	public void setZZPACK(String zZPACK) {
		ZZPACK = zZPACK;
	}

	public String getZZCARD() {
		return ZZCARD;
	}

	public void setZZCARD(String zZCARD) {
		ZZCARD = zZCARD;
	}

	public String getSATNR() {
		return SATNR;
	}

	public void setSATNR(String sATNR) {
		SATNR = sATNR;
	}

	public String getMSTAV() {
		return MSTAV;
	}

	public void setMSTAV(String mSTAV) {
		MSTAV = mSTAV;
	}

	public String getZLAND() {
		return ZLAND;
	}

	public void setZLAND(String zLAND) {
		ZLAND = zLAND;
	}

	public String getZLOCAL() {
		return ZLOCAL;
	}

	public void setZLOCAL(String zLOCAL) {
		ZLOCAL = zLOCAL;
	}

	public String getZLY_FLAG() {
		return ZLY_FLAG;
	}

	public void setZLY_FLAG(String zLY_FLAG) {
		ZLY_FLAG = zLY_FLAG;
	}

	public String getZZSSDATE() {
		return ZZSSDATE;
	}

	public void setZZSSDATE(String zZSSDATE) {
		ZZSSDATE = zZSSDATE;
	}

	public String getGOODCLASS() {
		return GOODCLASS;
	}

	public void setGOODCLASS(String gOODCLASS) {
		GOODCLASS = gOODCLASS;
	}

	public String getZZGENDER() {
		return ZZGENDER;
	}

	public void setZZGENDER(String zZGENDER) {
		ZZGENDER = zZGENDER;
	}

	public String getTAXKM1() {
		return TAXKM1;
	}

	public void setTAXKM1(String tAXKM1) {
		TAXKM1 = tAXKM1;
	}

	public String getTAXKM2() {
		return TAXKM2;
	}

	public void setTAXKM2(String tAXKM2) {
		TAXKM2 = tAXKM2;
	}

	public String getZZYCBZ() {
		return ZZYCBZ;
	}

	public void setZZYCBZ(String zZYCBZ) {
		ZZYCBZ = zZYCBZ;
	}

	public String getWERKS() {
		return WERKS;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public String getSAISO() {
		return SAISO;
	}

	public void setSAISO(String sAISO) {
		SAISO = sAISO;
	}

	public String getZZPICCODE() {
		return ZZPICCODE;
	}

	public void setZZPICCODE(String zZPICCODE) {
		ZZPICCODE = zZPICCODE;
	}

	public String getIS_GIFT() {
		return IS_GIFT;
	}

	public void setIS_GIFT(String iS_GIFT) {
		IS_GIFT = iS_GIFT;
	}

	public String getZZVDBC() {
		return ZZVDBC;
	}

	public void setZZVDBC(String zZVDBC) {
		ZZVDBC = zZVDBC;
	}

	public String getZSPRICE() {
		return ZSPRICE;
	}

	public void setZSPRICE(String zSPRICE) {
		ZSPRICE = zSPRICE;
	}

	public String getACTIONCODE() {
		return ACTIONCODE;
	}

	public void setACTIONCODE(String aCTIONCODE) {
		ACTIONCODE = aCTIONCODE;
	}

	public String getACTIONDATE() {
		return ACTIONDATE;
	}

	public void setACTIONDATE(String aCTIONDATE) {
		ACTIONDATE = aCTIONDATE;
	}

	public String getACTIONPERSON() {
		return ACTIONPERSON;
	}

	public void setACTIONPERSON(String aCTIONPERSON) {
		ACTIONPERSON = aCTIONPERSON;
	}

	public String getTAXKM3() {
		return TAXKM3;
	}

	public void setTAXKM3(String tAXKM3) {
		TAXKM3 = tAXKM3;
	}

	@Override
	public String toString() {
		return "ProductsSAPERP [MTART=" + MTART + ", MATKL=" + MATKL + ", ATTYP=" + ATTYP
				+ ", MATNR=" + MATNR + ", S_MATNR=" + S_MATNR + ", MAKTX=" + MAKTX + ", LIFNR="
				+ LIFNR + ", ZGID=" + ZGID + ", ZZVDMAT=" + ZZVDMAT + ", ZZWLLX=" + ZZWLLX
				+ ", ZZBRAND_ID=" + ZZBRAND_ID + ", ZZXXHC_FLAG=" + ZZXXHC_FLAG + ", ZZDKNO="
				+ ZZDKNO + ", ZZPRICE=" + ZZPRICE + ", ZCOLOR=" + ZCOLOR + ", ZZCOLORCODE="
				+ ZZCOLORCODE + ", ZSIZE=" + ZSIZE + ", ZZSIZECODE=" + ZZSIZECODE + ", MEINS="
				+ MEINS + ", ZZCOD=" + ZZCOD + ", ZZPACK=" + ZZPACK + ", ZZCARD=" + ZZCARD
				+ ", SATNR=" + SATNR + ", MSTAV=" + MSTAV + ", ZLAND=" + ZLAND + ", ZLOCAL="
				+ ZLOCAL + ", ZLY_FLAG=" + ZLY_FLAG + ", ZZSSDATE=" + ZZSSDATE + ", GOODCLASS="
				+ GOODCLASS + ", ZZGENDER=" + ZZGENDER + ", TAXKM1=" + TAXKM1 + ", TAXKM2="
				+ TAXKM2 + ", TAXKM3=" + TAXKM3 + ", ZZYCBZ=" + ZZYCBZ + ", WERKS=" + WERKS
				+ ", SAISO=" + SAISO + ", ZZPICCODE=" + ZZPICCODE + ", IS_GIFT=" + IS_GIFT
				+ ", ZZVDBC=" + ZZVDBC + ", ZSPRICE=" + ZSPRICE + ", COLORSID=" + COLORSID
				+ ", UNIT=" + UNIT + ", OFFERNUMBER=" + OFFERNUMBER + ", RATE_PRICE=" + RATE_PRICE
				+ ", SCATE=" + SCATE + ", ACTIONCODE=" + ACTIONCODE + ", ACTIONDATE=" + ACTIONDATE
				+ ", ACTIONPERSON=" + ACTIONPERSON + "]";
	}

}
