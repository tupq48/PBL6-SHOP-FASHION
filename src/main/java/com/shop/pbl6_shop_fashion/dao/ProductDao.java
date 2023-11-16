package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.config.DatabaseConfig;
import com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto;
import com.shop.pbl6_shop_fashion.dto.ProductMobile;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import com.sun.jdi.IntegerType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Transformer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class ProductDao {

    public List<Product> searchAllProducts() {
        return ConnectionProvider.openSession().createNativeQuery("select * from products", Product.class).getResultList();
    }

    public ProductDetailMobileDto searchDetailProducts(Integer id) {
//        databaseConfig.dataSource();
        String sql="with AnhSanPham AS (\n" +
                "\tselect pr.*,group_concat(pi.url) as Link_anh \n" +
                "    from products pr \n" +
                "    join product_images pi \n" +
                "    on pi.product_id=pr.id group by pr.id),\n" +
                "Sizes as( select group_concat(ps.quantity) as SoLuongConLai, pr.id as Id_sp,  group_concat(s.id) as loai_size, group_concat(s.name) as ten_size from product_size ps \n" +
                "             left join products pr on pr.id = ps.product_id \n" +
                "              left join sizes s on s.id = ps.size_id\n" +
                "              group by pr.id),\n" +
                "CMT_US as(select group_concat(cmt.content) as noidungcmt, group_concat(cmt.create_at) as ngaytaocmt, group_concat(us.full_name)as nguoicmt, group_concat(us.url_image) as Avatar,pr.id as idsp, pr.name\n" +
                "from products pr left join comments cmt \n" +
                "on pr.id = cmt.product_id left join users us on us.id = cmt.user_id\n" +
                "group by pr.id),\n" +
                "KM as(select pr.id as SPID, group_concat(ps.discount_value) as discount_value, group_concat(ps.discount_type) as discount_type\n" +
                "                from products pr\n" +
                "                left join promotions ps on pr.promotion_id= ps.id\n" +
                "                group by pr.id)\n" +
                "\n" +
                " select ct.id as Loai_san_pham,ct.name as Ten_loại_san_pham,br.id AS Ma_thuong_hieu, br.name as Ten_thuong_hieu,\n" +
                "                pr.id, pr.name, pr.price, pr.quantity,pr.quantity_sold,pr.description,Link_anh,ngaytaocmt, noidungcmt,nguoicmt,Avatar,loai_size, ten_size,SoLuongConLai,discount_value,discount_type\n" +
                "       from products pr \n" +
                "       join brands br on br.id = pr.brand_id \n" +
                "       join categories ct on ct.id= pr.category_id \n" +
                "       join product_size psi on psi.product_id=pr.id\n" +
                "\t   LEFT JOIN AnhSanPham asp on pr.id=asp.id\n" +
                "\tjoin Sizes siz on siz.Id_sp = pr.id\n" +
                "       LEFT join CMT_US on pr.id = CMT_US.idsp\n" +
                "       LEFT join KM on pr.id = KM.SPID\n" +
                "\t   where pr.id = ?\t   \n" +
                "       group by pr.id;";

        // Tạo truy vấn native SQL
        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        query.setParameter(1, id);

        // Thực hiện truy vấn
        List<Object[]> results = query.getResultList();
        System.out.println("result:" + results.size());
        ProductDetailMobileDto product = null;

        for (Object[] result : results) {
            product = new ProductDetailMobileDto();
            product.setCategoryType((Integer) result[0]);
            product.setCategoryName((String) result[1]);
            product.setBrandType((Integer) result[2]);
            product.setBrandName((String) result[3]);
            product.setProductId((Integer) result[4]);
            product.setProductName((String) result[5]);
            product.setPrice((Long) result[6]);
            product.setQuantity_sold((Long) result[7]);
            product.setQuantity((Long) result[8]);
            product.setDecription((String) result[9]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            List<String> createDate = new ArrayList<>();

            String dateString = (String) result[11]; // Lấy chuỗi ngày tháng từ result
            if( dateString != null) {
                createDate = List.of(dateString.split(","));
            }
            System.out.println("ngay tao;" + createDate);
            List<Date> createDateList = new ArrayList<>();

            for (String dateString1 : createDate) {
                try {
                    Date date = dateFormat.parse(dateString1);
                    createDateList.add(date);
                    product.setCommentCreatedAts(createDateList);
                } catch (ParseException e) {
                    System.err.println("Lỗi trong việc phân tích chuỗi ngày tháng: " + e.getMessage());
                }
            }
            String comments = (String) result[12];
            String imageUrls = (String) result[10];
            String usernames = (String) result[13];
            String avatar = (String) result[14];
            String sizeTypes = (String) result[15];
            String sizeNames = (String) result[16];
            String SizeQuantity = (String) result[17];
            List<String> cmtContent = new ArrayList<>();
            if( dateString != null) {
                cmtContent = List.of(comments.split(","));
            }
            List<String> usernamesList = new ArrayList<>();
            if(usernames != null){
                usernamesList = List.of(usernames.split(","));
            }
            List<String> avatarList = new ArrayList<>();
            if( avatar != null) {
                avatarList = List.of(avatar.split(","));
            }
            List<String> imageUrlArray = new ArrayList<>();
            if(imageUrls != null) {
                imageUrlArray = List.of(imageUrls.split(","));
            }

            List<String> sizeTypeList = new ArrayList<>();
            if(sizeTypes != null) {
                sizeTypeList = List.of(sizeTypes.split(","));
            }

            List<String> sizeNameList = new ArrayList<>();
            if(sizeNames != null) {
                sizeNameList = List.of(sizeNames.split(","));
            }
             List<String> SizeQuantityList = new ArrayList<>();
             if(SizeQuantity != null) {
                 SizeQuantityList = List.of(SizeQuantity.split(","));
             }

            product.setCommentContents(cmtContent);
            product.setProductUrls(imageUrlArray);
            product.setCommentUsers(usernamesList);
            product.setAvatarUsers(avatarList);
            product.setSizeTypes(sizeTypeList);
            product.setSizeNames(sizeNameList);
            product.setSizeQuantity(SizeQuantityList);

            List<String> discountValueList = new ArrayList<>();
            String discountValue = (String) result[17];
            if( discountValue != null) {
                discountValueList = List.of(discountValue.split(","));
            }
            String discount_type = (String) result[18];
            List<String> discountTypeList = new ArrayList<>();

            if( discount_type != null) {
                discountTypeList = List.of(discount_type.split(","));
            }
            Long price_pro = (Long) result[6];
            for (int i =0; i <discountTypeList.size(); i++){
                if (discountTypeList.get(i).equals("AMOUNT")) {
                    price_pro = price_pro - Long.parseLong(discountValueList.get(i));
                }
                if (discountTypeList.get(i).equals("PERCENTAGE")) {
                    price_pro = price_pro - (price_pro * Long.parseLong(discountValueList.get(i))/100);

                }
            }
            product.setPrice_promote(price_pro);

        }

        return product;
    }
    public List<ProductMobile> getProductsMobile(){
        String sql="with AnhSanPham AS (\n" +
                "                select pr.*,group_concat(pi.url) as Link_anh \n" +
                "                from products pr \n" +
                "                join product_images pi on pi.product_id=pr.id \n" +
                "                group by pr.id)\n" +
                "select pr.id,pr.name, pr.price, pr.quantity, pr.quantity_sold,group_concat(ps.discount_value), group_concat(ps.discount_type), link_anh\n" +
                "from products pr \n" +
                "left join promotions ps on pr.promotion_id= ps.id\n" +
                "join AnhSanPham asp on asp.id = pr.id\n" +
                "group by pr.id;";
        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        System.out.println("result:" + results.size());
        List<ProductMobile> products = new ArrayList<>();
        for (Object[] result:results){
            ProductMobile product = new ProductMobile();
            product.setProduct_name((String) result[1]);
            product.setProduct_id((Integer) result[0]);
            product.setPrice((Long) result[2]);
            product.setQuantity((Long) result[3]);
            product.setQuantity_sold((Long) result[4]);
            List<String> discountValueList = new ArrayList<>();
            String discountValue = (String) result[5];
            if( discountValue != null) {
                discountValueList = List.of(discountValue.split(","));
            }
            String discount_type = (String) result[6];
            List<String> discountTypeList = new ArrayList<>();

            if( discount_type != null) {
                discountTypeList = List.of(discount_type.split(","));
            }
            Long proce_pro = (Long) result[2];
            for (int i =0; i <discountTypeList.size(); i++){
                    if (discountTypeList.get(i).equals("AMOUNT")) {
                        proce_pro = proce_pro - Long.parseLong(discountValueList.get(i));
                    }
                    if (discountTypeList.get(i).equals("PERCENTAGE")) {
                        proce_pro = proce_pro - (proce_pro * Long.parseLong(discountValueList.get(i))/100);

                    }
            }
            product.setPrice_promote(proce_pro);
            String images = (String) result[7];
            List<String> imagesList = List.of(images.split(","));
            product.setProduct_image(imagesList);
            products.add(product);
        }
        return products;
    }

    public List<ProductMobile> searchProductsMobile(String keyword, Integer minprice,Integer maxprice, String category){
//        String sql = "WITH AnhSanPham AS ( "
//                + "  SELECT pr.*, GROUP_CONCAT(pi.url) AS Link_anh "
//                + "  FROM products pr "
//                + "  LEFT JOIN product_images pi ON pi.product_id = pr.id "
//                + "  GROUP BY pr.id) "
//                + "SELECT pr.id, pr.name, pr.price, pr.quantity, pr.quantity_sold, "
//                + "       GROUP_CONCAT(ps.discount_value), GROUP_CONCAT(ps.discount_type), link_anh "
//                + "FROM products pr "
//                + "LEFT JOIN promotion_product prp ON pr.id = prp.product_id "
//                + "LEFT JOIN promotions ps ON prp.promotion_id = ps.id "
//                + "JOIN AnhSanPham asp ON asp.id = pr.id "
//                + "WHERE pr.name LIKE :keyword "
//                + "GROUP BY pr.id";
        String sql="with AnhSanPham AS (\n" +
                "        select pr.*,group_concat(pi.url) as Link_anh \n" +
                "               from products pr \n" +
                "                          left join product_images pi on pi.product_id=pr.id \n" +
                "                group by pr.id)\n" +
                "                select pr.id,pr.name, pr.price, pr.quantity, pr.quantity_sold,group_concat(ps.discount_value), group_concat(ps.discount_type), link_anh\n" +
                "                from products pr\n" +
                 "LEFT JOIN promotions ps ON pr.promotion_id = ps.id "     +           "                join AnhSanPham asp on asp.id = pr.id\n" +
                "                join categories ct on ct.id = pr.category_id\n" +
                "                where pr.name like :keyword and pr.price BETWEEN :minprice AND :maxprice and ct.name like :category \n" +
                "                group by pr.id";

        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        query.setParameter("keyword","%" +keyword+"%");
        query.setParameter("category","%" +category+"%");
        query.setParameter("minprice", minprice);
        query.setParameter("maxprice", maxprice);




        List<Object[]> results = query.getResultList();
        System.out.println("result:" + results.size());
        List<ProductMobile> products = new ArrayList<>();
        for (Object[] result:results){
            ProductMobile product = new ProductMobile();
            product.setProduct_name((String) result[1]);
            product.setProduct_id((Integer) result[0]);
            product.setPrice((Long) result[2]);
            product.setQuantity((Long) result[3]);
            product.setQuantity_sold((Long) result[4]);
            List<String> discountValueList = new ArrayList<>();
            String discountValue = (String) result[5];
            if( discountValue != null) {
                discountValueList = List.of(discountValue.split(","));
            }
            String discount_type = (String) result[6];
            List<String> discountTypeList = new ArrayList<>();

            if( discount_type != null) {
                discountTypeList = List.of(discount_type.split(","));
            }
            Long proce_pro = (Long) result[2];
            for (int i =0; i <discountTypeList.size(); i++){
                if (discountTypeList.get(i).equals("AMOUNT")) {
                    proce_pro = proce_pro - Long.parseLong(discountValueList.get(i));
                }
                if (discountTypeList.get(i).equals("PERCENTAGE")) {
                    proce_pro = proce_pro - (proce_pro * Long.parseLong(discountValueList.get(i))/100);

                }
            }
            product.setPrice_promote(proce_pro);
            String images = (String) result[7];
            List<String> imagesList= new ArrayList<>();
            if(images != null){
                imagesList = List.of(images.split(","));
            }
            product.setProduct_image(imagesList);
            products.add(product);
        }
        return products;
    }
}
