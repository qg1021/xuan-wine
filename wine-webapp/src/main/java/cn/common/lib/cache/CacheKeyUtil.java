package cn.common.lib.cache;

/**
 * 
 * 缓存工具类
 * 
 * @author qingang
 * @version 1.0
 * @since 2011-11-9
 */
public class CacheKeyUtil
{

    public static String cachePerfix        = "machine_";

    /** 基础数据Key **/

    public static String BaseDataPrefix     = cachePerfix + "BaseData";

    /** 招商信息Key **/

    public static String BusinessPrefix     = cachePerfix + "Business";

    /** 求购Key **/

    public static String BuyPrefix          = cachePerfix + "Buy";

    /** 供应Key **/

    public static String SupplyPrefix       = cachePerfix + "Supply";

    /** 平台简介、联系我们Key **/

    public static String IntroductionPrefix = cachePerfix + "Introduction";

    /** 友情链接、广告推广Key **/

    public static String LinksPrefix        = cachePerfix + "Links";

    /** 新闻焦点、热点资讯信息Key **/
    public static String NewsPrefix         = cachePerfix + "News";

    /** 产品分类key **/
    public static String CategoryPrefix     = cachePerfix + "Category";

    /** 广告位key **/
    public static String AdvertPrefix       = cachePerfix + "Advert";

    /** 企业信息key **/
    public static String CompanyPrefix      = cachePerfix + "Company";

    /** 产品key **/
    public static String ProductPrefix      = cachePerfix + "Product";

    /**
     * 构建所有分类cachekey
     * 
     * @return
     */
    public static String buildCategoryKey(Long parentId)
    {
        if (parentId == null || parentId == 0)
        {
            return CategoryPrefix + "_" + "allcateogry";
        }
        else
        {
            return CategoryPrefix + "_" + parentId;
        }
    }

    public static String buildCategoryKey(String rkey)
    {
        return CategoryPrefix + "_" + rkey;
    }

    public static String buildCompanyKey(int type)
    {
        return CompanyPrefix + "_" + type;
    }

    public static String buildProductKey(long thirdCat, int pageSize)
    {
        return ProductPrefix + "_" + thirdCat + "_" + pageSize;
    }

    public static String buildRecommendProductKey(int pageSize)
    {
        return ProductPrefix + "_Recommend" + "_" + pageSize;
    }

    public static String buildNewsKey(int type)
    {
        return NewsPrefix + "_" + type;
    }

    public static String buildAdvertKey(int type)
    {
        return AdvertPrefix + "_" + type;
    }

    public static String buildLinksKey(int type)
    {
        return LinksPrefix + "_" + type;
    }

    public static String buildBuyKey(int type)
    {
        return BuyPrefix + "_" + type;
    }

    public static String buildSupplyKey(int type)
    {
        return SupplyPrefix + "_" + type;
    }

    public static String buildIntroductionKey(int type)
    {
        return IntroductionPrefix + "_" + type;
    }

    public static String buildBaseDataKey(int type)
    {
        return BaseDataPrefix + "_" + type;
    }

    public static String buildBusinessKey(int pagesize)
    {
        return BusinessPrefix + "_" + pagesize;
    }

}
