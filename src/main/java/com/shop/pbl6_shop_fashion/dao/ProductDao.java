package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.config.DatabaseConfig;
import com.shop.pbl6_shop_fashion.dto.PaginationResponse;
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
        String sql="WITH AnhSanPham AS (\n" +
                "    SELECT\n" +
                "        pr.*,\n" +
                "        GROUP_CONCAT(pi.url) AS Link_anh\n" +
                "    FROM\n" +
                "        products pr\n" +
                "    JOIN product_images pi ON pi.product_id = pr.id\n" +
                "    GROUP BY\n" +
                "        pr.id\n" +
                "),\n" +
                "Sizes AS (\n" +
                "    SELECT\n" +
                "        GROUP_CONCAT(ps.quantity) AS SoLuongConLai,\n" +
                "        pr.id AS Id_sp,\n" +
                "        GROUP_CONCAT(s.id) AS loai_size,\n" +
                "        GROUP_CONCAT(s.name) AS ten_size\n" +
                "    FROM\n" +
                "        product_size ps\n" +
                "    LEFT JOIN products pr ON pr.id = ps.product_id\n" +
                "    LEFT JOIN sizes s ON s.id = ps.size_id\n" +
                "    GROUP BY\n" +
                "        pr.id\n" +
                "),\n" +
                "CMT_US AS (\n" +
                "    SELECT\n" +
                "        GROUP_CONCAT(cmt.content) AS noidungcmt,\n" +
                "        GROUP_CONCAT(cmt.create_at) AS ngaytaocmt,\n" +
                "        GROUP_CONCAT(us.full_name) AS nguoicmt,\n" +
                "        GROUP_CONCAT(us.url_image) AS Avatar,\n" +
                "        pr.id AS idsp,\n" +
                "        pr.name,\n" +
                "        GROUP_CONCAT(cmt.rate) as star\n" +
                "    FROM\n" +
                "        products pr\n" +
                "    LEFT JOIN comments cmt ON pr.id = cmt.product_id\n" +
                "    LEFT JOIN users us ON us.id = cmt.user_id\n" +
                "    GROUP BY\n" +
                "        pr.id\n" +
                "),\n" +
                "KM AS (\n" +
                "    SELECT\n" +
                "        pr.id AS SPID,\n" +
                "        GROUP_CONCAT(ps.discount_value) AS discount_value,\n" +
                "        GROUP_CONCAT(ps.discount_type) AS discount_type\n" +
                "    FROM\n" +
                "        products pr\n" +
                "    LEFT JOIN promotions ps ON pr.promotion_id = ps.id\n" +
                "    GROUP BY\n" +
                "        pr.id\n" +
                ")\n" +
                "SELECT\n" +
                "    ct.id AS Loai_san_pham,\n" +
                "    ct.name AS Ten_loai_san_pham,\n" +
                "    br.id AS Ma_thuong_hieu,\n" +
                "    br.name AS Ten_thuong_hieu,\n" +
                "    pr.id,\n" +
                "    pr.name,\n" +
                "    pr.price,\n" +
                "    pr.quantity,\n" +
                "    pr.quantity_sold,\n" +
                "    pr.description,\n" +
                "    Link_anh,\n" +
                "    ngaytaocmt,\n" +
                "    noidungcmt,\n" +
                "    nguoicmt,\n" +
                "    Avatar,\n" +
                "    loai_size,\n" +
                "    ten_size,\n" +
                "    SoLuongConLai,\n" +
                "    discount_value,\n" +
                "    discount_type, star\n" +
                "FROM\n" +
                "    products pr\n" +
                "JOIN brands br ON br.id = pr.brand_id\n" +
                "JOIN categories ct ON ct.id = pr.category_id\n" +
                "left JOIN product_size psi ON psi.product_id = pr.id\n" +
                "LEFT JOIN AnhSanPham asp ON pr.id = asp.id\n" +
                "JOIN Sizes siz ON siz.Id_sp = pr.id\n" +
                "LEFT JOIN CMT_US ON pr.id = CMT_US.idsp\n" +
                "LEFT JOIN KM ON pr.id = KM.SPID\n" +
                "WHERE\n" +
                "    pr.id = ? "+
                "GROUP BY\n" +
                "    pr.id;";

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
            String star = (String) result[20];
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
            List<String> rateList = new ArrayList<>();
            if( star != null) {
                rateList = List.of(star.split(","));
            }
            List<Integer> rateListInteger = convertStringListToIntegerList(rateList);

            product.setCommentContents(cmtContent);
            product.setProductUrls(imageUrlArray);
            product.setCommentUsers(usernamesList);
            product.setAvatarUsers(avatarList);
            product.setSizeTypes(sizeTypeList);
            product.setSizeNames(sizeNameList);
            product.setSizeQuantity(SizeQuantityList);
            product.setRate(rateListInteger);
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
            Double avgRate=0.0;
            for(int i=0; i< rateListInteger.size();i++){
                avgRate+=rateListInteger.get(i);
            }
            if(rateListInteger.size()> 0){
                avgRate= avgRate/rateListInteger.size();
            }
            product.setAvgRate(avgRate);
            product.setPrice_promote(price_pro);

        }

        return product;
    }
    public List<ProductMobile> getAllProducts(int page, int pageSize){
        int firstResult = (page - 1) * pageSize;
        String sql="WITH AnhSanPham AS (\n" +
                "    SELECT pr.*,GROUP_CONCAT(pi.url) AS Link_anh\n" +
                "    FROM products pr\n" +
                "    JOIN product_images pi ON pi.product_id = pr.id\n" +
                "    GROUP BY pr.id\n" +
                ")\n" +
                "SELECT pr.id,pr.name,pr.price,pr.quantity,pr.quantity_sold,\tGROUP_CONCAT(ps.discount_value) AS discount_values,GROUP_CONCAT(ps.discount_type) AS discount_types,link_anh,\n" +
                "    pr.category_id as Loai,ct.name as Ten_loai,pr.brand_id as Thuong_hieu,br.name as Ten_thuong_hieu\n" +
                "FROM products pr\n" +
                "LEFT JOIN promotions ps ON pr.promotion_id = ps.id\n" +
                "LEFT JOIN AnhSanPham asp ON asp.id = pr.id\n" +
                "LEFT JOIN categories ct on ct.id = pr.category_id\n" +
                "LEFT JOIN brands br on br.id = pr.brand_id\n" +
                "GROUP BY pr.id;";
        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
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
            product.setCategory_id((Integer) result[8]);
            product.setCategory_id((Integer) result[8]);
            product.setCategory_name((String) result[9]);
            product.setBrand_id((Integer) result[10]);
            product.setBrand_name((String) result[11]);

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
            List<String> imagesList = new ArrayList<>();
            if(images != null){
                imagesList = List.of(images.split(","));
            }
            product.setProduct_image(imagesList);
            products.add(product);
        }
        return products;
    }

    public PaginationResponse<ProductMobile> getProductsByCategoryorBrand(Integer category_id, Integer brand_id, int page, int pageSize){
        int firstResult = (page - 1) * pageSize;
        String sql="WITH AnhSanPham AS (\n" +
                "    SELECT pr.*,GROUP_CONCAT(pi.url) AS Link_anh\n" +
                "    FROM products pr\n" +
                "    JOIN product_images pi ON pi.product_id = pr.id\n" +
                "    GROUP BY pr.id\n" +
                ")\n" +
                "SELECT pr.id,pr.name,pr.price,pr.quantity,pr.quantity_sold,\tGROUP_CONCAT(ps.discount_value) AS discount_values,GROUP_CONCAT(ps.discount_type) AS discount_types,link_anh,\n" +
                "    pr.category_id as Loai,ct.name as Ten_loai,pr.brand_id as Thuong_hieu,br.name as Ten_thuong_hieu, br.image_url as Anh_thuong_hieu, ct.image_url as Anh_Loai_sp\n" +
                "FROM products pr\n" +
                "LEFT JOIN promotions ps ON pr.promotion_id = ps.id\n" +
                "LEFT JOIN AnhSanPham asp ON asp.id = pr.id\n" +
                "LEFT JOIN categories ct on ct.id = pr.category_id\n" +
                "LEFT JOIN brands br on br.id = pr.brand_id\n";

        if(category_id != 0 && brand_id == 0){
            sql+="where ct.id= ?\n" +
                    "GROUP BY pr.id;";


        }
        else if(brand_id != 0 && category_id == 0){
            sql+="where br.id= ?\n" +
                    "GROUP BY pr.id;";
        } else if (brand_id != 0 && category_id!=0) {
            sql+= "where br.id=? and ct.id=?\n" +
                    "GROUP BY pr.id;";
        }
        Query query = ConnectionProvider.openSession().createNativeQuery(sql);

        if(brand_id !=0 && category_id == 0){
            query.setParameter(1,brand_id);
        }
        else if(category_id != 0 && brand_id ==0){
            query.setParameter(1,category_id);
        } else if (category_id !=0 && brand_id !=0) {
            query.setParameter(1,brand_id);
            query.setParameter(2,category_id);
        }
        List<Object[]> results = query.getResultList();
        int totalItems = results.size();

        Double totalPage = Math.ceil(results.size()/(double)pageSize);
        System.out.println("total:" + totalPage);
        query.setMaxResults(pageSize);
        query.setFirstResult(firstResult);
        results = query.getResultList();
        System.out.println("result:" + results.size());
        List<ProductMobile> products = new ArrayList<>();
        for (Object[] result:results){
            ProductMobile product = new ProductMobile();
            product.setProduct_name((String) result[1]);
            product.setProduct_id((Integer) result[0]);
            product.setPrice((Long) result[2]);
            product.setQuantity((Long) result[3]);
            product.setQuantity_sold((Long) result[4]);
            product.setDiscount_value((String) result[5]);
            product.setDiscount_type((String) result[6]);
            product.setCategory_id((Integer) result[8]);
            product.setCategory_id((Integer) result[8]);
            product.setCategory_name((String) result[9]);
            product.setBrand_id((Integer) result[10]);
            product.setBrand_name((String) result[11]);
            product.setImg_brand((String) result[12]);
            product.setImg_category((String) result[13]);

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
            if(proce_pro < 0){
                product.setPrice_promote(Long.parseLong("0"));
            }
            else{
                product.setPrice_promote(proce_pro);
            }
            String images = (String) result[7];
            List<String> imagesList = new ArrayList<>();
            if(images != null){
                imagesList = List.of(images.split(","));
            }
            product.setProduct_image(imagesList);
            products.add(product);
        }
        PaginationResponse<ProductMobile> response = new PaginationResponse<>();
        response.setItems(products);
        response.setTotalItems(totalItems);
        response.setCurrentPage(page);
        response.setTotalPages(totalPage);
        response.setPageSize(pageSize);

        // Trả về đối tượng phản hồi
        return response;
    }

    public List<ProductMobile> searchProductsMobile(String keyword, Integer minprice,Integer maxprice, String category){
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
    private static List<Integer> convertStringListToIntegerList(List<String> stringList) {
        List<Integer> integerList = new ArrayList<>();

        for (String str : stringList) {
            try {
                // Chuyển đổi từ chuỗi sang số nguyên
                int number = Integer.parseInt(str);
                // Thêm số nguyên vào danh sách
                integerList.add(number);
            } catch (NumberFormatException e) {
                // Xử lý nếu chuỗi không thể chuyển đổi thành số nguyên
                System.err.println("Không thể chuyển đổi chuỗi thành số nguyên: " + str);
            }
        }

        return integerList;
    }

    public List<ProductMobile> getBestSellingProducts(Integer limit) {
        String sql="WITH AnhSanPham AS (\n" +
                "    SELECT pr.*, GROUP_CONCAT(pi.url) AS Link_anh\n" +
                "    FROM products pr\n" +
                "    JOIN product_images pi ON pi.product_id = pr.id\n" +
                "    GROUP BY pr.id\n" +
                ")\n" +
                "\n" +
                "SELECT \n" +
                "    pr.id,\n" +
                "    pr.name,\n" +
                "    pr.price,\n" +
                "    pr.quantity,\n" +
                "    pr.quantity_sold,\n" +
                "    GROUP_CONCAT(ps.discount_value) AS discount_values,\n" +
                "    GROUP_CONCAT(ps.discount_type) AS discount_types,\n" +
                "    link_anh,\n" +
                "    pr.category_id AS Loai,\n" +
                "    ct.name AS Ten_loai,\n" +
                "    pr.brand_id AS Thuong_hieu,\n" +
                "    br.name AS Ten_thuong_hieu\n" +
                "FROM\n" +
                "    products pr\n" +
                "    LEFT JOIN promotions ps ON pr.promotion_id = ps.id\n" +
                "    LEFT JOIN AnhSanPham asp ON asp.id = pr.id\n" +
                "    LEFT JOIN categories ct ON ct.id = pr.category_id\n" +
                "    LEFT JOIN brands br ON br.id = pr.brand_id\n" +
                "GROUP BY pr.id\n" +
                "    order by pr.quantity_sold desc\n" +
                "    limit ?\n" +
                ";\n";

        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        query.setParameter(1,limit);
        List<Object[]> results = query.getResultList();
        List<ProductMobile> products = new ArrayList<>();
        for (Object[] result:results){
            ProductMobile product = new ProductMobile();
            product.setProduct_name((String) result[1]);
            product.setProduct_id((Integer) result[0]);
            product.setPrice((Long) result[2]);
            product.setQuantity((Long) result[3]);
            product.setQuantity_sold((Long) result[4]);
            product.setCategory_id((Integer) result[8]);
            product.setCategory_id((Integer) result[8]);
            product.setCategory_name((String) result[9]);
            product.setBrand_id((Integer) result[10]);
            product.setBrand_name((String) result[11]);

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
            List<String> imagesList = new ArrayList<>();
            if(images != null){
                imagesList = List.of(images.split(","));
            }
            product.setProduct_image(imagesList);
            products.add(product);
        }
        return products;
    }
}
