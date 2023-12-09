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
    public com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto searchProductDetail(Integer id) {
        com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto product = productDao.searchDetailProducts(id);
        System.out.println("product service: " + product);
        return  product;
    }
    public List<ProductMobile> getAllProducts(int page, int pageSize){

        return productDao.getAllProducts(page,pageSize);
    }
    public List<ProductMobile> getProductsByCategoryorBrand(Integer category_id,Integer brand_id,int page, int pageSize){

        return productDao.getProductsByCategoryorBrand(category_id,brand_id,page,pageSize);
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

        System.out.println("upload anh : " + LocalDateTime.now());

        List<String> imageUrls = GoogleDriveUtils.uploadImages(images);
        List<ProductImage> productImages = new ArrayList<>();
        imageUrls.forEach(imageUrl -> {
            productImages.add(ProductImage.builder().url(imageUrl).product(product).build());
        });

        System.out.println("save product : " + LocalDateTime.now());

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
        product.setImages(productImages);
        product.setPromotion(promotion);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(null);

        productRepository.save(product);

        System.out.println("done :" + LocalDateTime.now());
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
        if (images != null) {
            System.out.println("upload image!");
            List<String> imageUrls = GoogleDriveUtils.uploadImages(images);
            List<ProductImage> productImages = new ArrayList<>();
            imageUrls.forEach(imageUrl -> {
                productImages.add(ProductImage.builder().url(imageUrl).product(product).build());
            });
            product.setImages(productImages);
            System.out.println("done!");
        }
        product.setUpdateAt(LocalDateTime.now());


        productRepository.save(product);
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
    public List<ProductMobile> searchProductsMobile(String keyword, Integer minprice, Integer maxprice, String category) {
        return  productDao.searchProductsMobile(keyword, minprice, maxprice, category);
    }


}
