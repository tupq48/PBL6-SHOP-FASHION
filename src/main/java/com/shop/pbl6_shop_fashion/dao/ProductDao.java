package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.ProductDetailDto;
import com.shop.pbl6_shop_fashion.entity.Product;
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
    @Autowired
    private SessionFactory sessionFactory;



    Session openSession() {
        return sessionFactory.openSession();
    }

    Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    public List<Product> searchAllProducts() {
        return openSession().createNativeQuery("select * from products", Product.class).getResultList();
    }
    public List<Product>
    public ProductDetailDto searchDetailProducts(Integer id) {

        String sql="with AnhSanPham AS (\n" +
                "\t\t\t \t  select pr.*,group_concat(pi.url) as Link_anh from products pr join product_images pi on pi.product_id=pr.id group by pr.id)\n" +
                "     \n" +
                "\n" +
                "     select ct.id as Loai_san_pham,ct.name as Ten_loại_san_pham,br.id AS Ma_thuong_hieu, br.name as Ten_thuong_hieu,\n" +
                "\t pr.\n" +
                "id, pr.name, pr.price, pr.description,Link_anh, group_concat(cmt.create_at),\n" +
                "\t group_concat(cmt.content), group_concat(us.username) as nguoi_binh_luan from products pr join brands br on br.id = pr.brand_id join categories ct on ct.id= pr.category_id join comments cmt on cmt.product_id =pr.id join users us on us.id = cmt.user_id\n" +
                "\t JOIN AnhSanPham asp on pr.id=asp.id\n" +
                "     where pr.id =?  group by pr.id";
        // Tạo truy vấn native SQL
        Query query = openSession().createNativeQuery(sql);
        query.setParameter(1, id);

        // Thực hiện truy vấn
        List<Object[]> results = query.getResultList();
        System.out.println("result:" + results.size());
        ProductDetailDto product = null;

        for (Object[] result : results) {
            product = new ProductDetailDto();
            product.setLoai_san_pham((Integer) result[0]);
            product.setLoaisanpham_ten((String) result[1]);
            product.setNhom_id((Integer) result[2]);
            product.setNhom_ten((String) result[3]);
            product.setSanpham_id((Integer) result[4]);
            product.setSanpham_ten((String) result[5]);
            product.setLohang_gia_ban_ra((Long) result[6]);
            product.setSanpham_mo_ta((String) result[7]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            String dateString = (String) result[9]; // Lấy chuỗi ngày tháng từ result
            List<String> createDate = List.of(dateString.split(","));
            System.out.println("ngay tao;" + createDate);
            List<Date> createDateList = new ArrayList<>();

            for (String dateString1 : createDate) {
                try {
                    Date date = dateFormat.parse(dateString1);
                    createDateList.add(date);
                    product.setBinhluan_created_at(createDateList);
                } catch (ParseException e) {
                    System.err.println("Lỗi trong việc phân tích chuỗi ngày tháng: " + e.getMessage());
                }
            }
//            product.setBinhluan_created_at((Date) result[10]);

            // Xử lý các trường khác tương tự
//            java.sql.Timestamp timestamp = (java.sql.Timestamp) result[9]; // Lấy java.sql.Timestamp từ result
//
//// Chuyển đổi từ Timestamp sang Date
//            Date date = new Date(timestamp.getTime());
//
//            product.setBinhluan_created_at(date); // Gán Date vào thuộc tính binhluan_created_at

            // Xử lý group_concat
            String comments = (String) result[10];
            String imageUrls = (String) result[8];
            String usernames = (String) result[11];

            // Xử lý chuỗi và gán giá trị cho product

            // Ví dụ: chia chuỗi thành danh sách các URL ảnh
            List<String> cmtContent = List.of(comments.split(","));
            List<String> usernamesList = List.of(usernames.split(","));
            List<String> imageUrlArray = List.of(imageUrls.split(","));

            System.out.println(imageUrlArray);
            product.setBinhluan_noi_dung(cmtContent);
            product.setSanpham_anh(imageUrlArray);
            product.setBinhluan_ten(usernamesList);


            // Tương tự cho các trường còn lại
        }

        return product;
    }
}
