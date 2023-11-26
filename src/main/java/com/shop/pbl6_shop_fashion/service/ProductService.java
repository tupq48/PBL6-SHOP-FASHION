package com.shop.pbl6_shop_fashion.service;


import com.shop.pbl6_shop_fashion.dao.*;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.ProductMobile;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.enums.SizeType;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    private ProductDao productDao;

    @Autowired
    EntityManager entityManager;

    public ProductDetailDto getProductDetailById(Integer id) {
        return productRepository.getProductDetailById(id);
    }

    public ProductPromotionDto getProductPromotionById(Integer id) {
        return productRepository.getProductPromotionById(id);
    }

    public List<ProductDto> getNewestProduct(Integer number) {
        return productRepository.getNewestProduct(number);
    }

    public List<ProductDto> getProductByCategoryAndPage(Integer categoryId, Integer page, Integer limit) {

        return productRepository.getProductByCategoryAndPage(categoryId, page, limit);
    }

/*
    private com.shop.pbl6_shop_fashion.dto.ProductDto convertProductToProductDto(Product product) {
        com.shop.pbl6_shop_fashion.dto.ProductDto pr = new com.shop.pbl6_shop_fashion.dto.ProductDto();
        pr.setId(product.getId());
        pr.setLomoi(0);
        pr.setSanpham_ten(product.getName());
        pr.setSanpham_anh("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.elle.vn%2Fwp-content%2Fuploads%2F2017%2F07%2F25%2Fhinh-anh-dep-1.jpg&tbnid=sWpclQpVCwLtqM&vet=12ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy..i&imgrefurl=https%3A%2F%2Fwww.elle.vn%2Fthe-gioi-van-hoa%2F26-hinh-anh-dep-den-nghet-tho-du-khong-chinh-sua-photoshop&docid=QBp5guTnuO5kkM&w=1000&h=667&q=%E1%BA%A3nh%20%C4%91%E1%BA%B9p&ved=2ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy");
        pr.setSanpham_url("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.elle.vn%2Fwp-content%2Fuploads%2F2017%2F07%2F25%2Fhinh-anh-dep-1.jpg&tbnid=sWpclQpVCwLtqM&vet=12ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy..i&imgrefurl=https%3A%2F%2Fwww.elle.vn%2Fthe-gioi-van-hoa%2F26-hinh-anh-dep-den-nghet-tho-du-khong-chinh-sua-photoshop&docid=QBp5guTnuO5kkM&w=1000&h=667&q=%E1%BA%A3nh%20%C4%91%E1%BA%B9p&ved=2ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy");
        pr.setSanpham_khuyenmai(pr.getSanpham_khuyenmai());
        pr.setLohang_gia_ban_ra(product.getPrice());
//        pr.setBrand(product.getBrand().getName());
//        pr.setDecription(product.getDescription());
//        pr.setName(product.getName());
//        pr.setPrice(product.getPrice());
//        pr.setStatus(product.getStatus());
//        pr.setQuantity(product.getQuantity());
//        pr.setQuantity_sold(product.getQuantitySold());
//        pr.setType(product.getCategory().getName());
//        pr.setUnit(product.getUnit());
        return pr;
    }
*/



    public com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto searchProductDetail(Integer id) {
        com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto product = productDao.searchDetailProducts(id);
        System.out.println("product service: " + product);
        return  product;
    }
    public List<ProductMobile> getProductsMobile(){

        return productDao.getProductsMobile();
    }

    public void updateImages(Integer id, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        files.forEach(file -> {
            String imageurl = GoogleDriveUtils.uploadImage(file);
            imageUrls.add(imageurl);
        });
        productRepository.updateProductImages(id, imageUrls);
    }

    public void addProduct(String name, String desc, Integer price, String unit, Integer brandId, Integer categoryId, List<String> productSizes, List<MultipartFile> images, Integer promotionId) {

        System.out.println("get brand cate : " + LocalDateTime.now());
        Product product = new Product();
        Brand brand = entityManager.find(Brand.class, brandId);
        Category category = entityManager.find(Category.class, categoryId);

        System.out.println("Xu ly du lieu: " + LocalDateTime.now());

        Integer quantity = 0;
        List<ProductSize> sizes = convertProductSizes(productSizes, product);
        for (ProductSize productSize : sizes) {
            quantity += productSize.getQuantity();
        }

        System.out.println("get promo : " + LocalDateTime.now());

        Promotion promotion = entityManager.find(Promotion.class, promotionId);



        product.setName(name);
        product.setDescription(desc);
        product.setStatus(null);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setQuantitySold(0);
        product.setBrand(brand);
        product.setCategory(category);
        product.setProductSizes(sizes);
        product.setComments(null);
        product.setPromotion(promotion);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(null);

        Product savedProduct = productRepository.save(product);

        int id = savedProduct.getId();
        Thread thread = new Thread(() -> {
            Product updateProduct = productRepository.findById(id).get();
            List<String> imageUrls = GoogleDriveUtils.uploadImages(images);
            List<ProductImage> productImages = new ArrayList<>();
            imageUrls.forEach(imageUrl -> {
                productImages.add(ProductImage.builder().url(imageUrl).product(updateProduct).build());
            });
            updateProduct.setImages(productImages);
            productRepository.save(updateProduct);
        });
        thread.start();

        return;
    }

    public void updateProduct(Integer productId, String name, String desc, Integer price, String unit, Integer brandId,
                              Integer categoryId, List<String> productSizes, List<MultipartFile> images, Integer promotionId) {
        Product product = entityManager.find(Product.class, productId);
        if (product == null) return;
        if (name != null) product.setName(name);
        if (desc != null) product.setDescription(desc);
        if (price != null) product.setPrice(price);
        if (unit != null) product.setUnit(unit);
        if (brandId != null) product.setBrand(entityManager.find(Brand.class, brandId));
        if (categoryId != null) product.setCategory(entityManager.find(Category.class, categoryId));
        if (productSizes != null) {
            List<ProductSize> currentSizes = product.getProductSizes();
            List<ProductSize> sizeList = convertProductSizes(productSizes, product);
            for (ProductSize currentSize : currentSizes) {
                for (ProductSize size : sizeList) {
                    if (currentSize.getSize() == size.getSize()) {
                        currentSize.setQuantity(size.getQuantity());
                        sizeList.remove(size);
                        break;
                    }
                }
            }
            product.setProductSizes(sizeList);

            Integer quantity = 0;
            for (ProductSize productSize : sizeList) {
                quantity += productSize.getQuantity();
            }
            product.setQuantity(quantity);
        }
        if (promotionId != null) product.setPromotion(entityManager.find(Promotion.class, promotionId));

        product.setUpdateAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        int id = savedProduct.getId();

        if (images != null) {
            Thread thread = new Thread(() -> {
                Product updateProduct = productRepository.findById(id).get();
                List<String> imageUrls = GoogleDriveUtils.uploadImages(images);
                List<ProductImage> productImages = new ArrayList<>();
                imageUrls.forEach(imageUrl -> {
                    productImages.add(ProductImage.builder().url(imageUrl).product(updateProduct).build());
                });
                updateProduct.setImages(productImages);
                productRepository.save(updateProduct);
            });
            thread.start();
        }
        return;
    }

    List<ProductSize> convertProductSizes(List<String> productSizes, Product product) {
        List<ProductSize> sizes = new ArrayList<>();
        for ( String str : productSizes) {
            SizeType sizeType = SizeType.valueOf(str.split(":")[0].trim());
            Integer quant = Integer.parseInt(str.split(":")[1]);
            ProductSize productSize = ProductSize.builder()
                    .size(sizeRepository.findByName(sizeType))
                    .quantity(quant)
                    .quantitySold(0)
                    .product(product)
                    .build();
            sizes.add(productSize);
        }
        return sizes;
    }
}
