INSERT INTO `brands` (id, description, name)
VALUES
    (1,'Nike Brand','Nike'),
    (2,'Adidas brand','Adidas'),
    (3,'Puma brand','Puma'),
    (4,'Fila brand','Fila'),
    (5,'Gucci brand','Gucci');

INSERT INTO `categories` (id, description, name)
VALUES
    (1,'Giay Nam','Giay Nam'),
    (2,'Sneakers','Sneakers'),
    (3,'Boots','Boots'),
    (4,'Giày Nữ','Giày Nữ'),
    (5,'Sandals','Sandals'),
    (6,'Giày Thể Thao','Giày Thể Thao'),
    (7,'Giày Cao Gót','Giày Cao Gót');

INSERT INTO `sizes`
VALUES
    (1,'Large size','LARGE'),
    (2,'Medium size','MEDIUM'),
    (3,'Small size','SMALL'),
    (4,'X-Large size','XLARGE'),
    (5,'XX-Large size','XXLARGE');

INSERT INTO `roles`
VALUES
    (1,'ADMIN'),
    (2,'USER'),
    (3,'MODERATOR');

INSERT INTO `users` (id, `address`, `create_at`, `full_name`, `gender`, `gmail`, `is_enabled`, `is_locked`, `password`, `phone_number`, `update_at`, `url_image`, `username`)
VALUES
    (1, '12 Minh Tri', NOW(), 'Tu', 'FEMALE', 'tupq@gmail.com', 1, 1, '123456', '0987547382', NOW(), null, 'tupq'),
    (2, '65 Duy Tan', NOW(), 'Hieu', 'MALE', 'hieu@gmail.com', 1, 1, '123456', '0775483921', NOW(), NULL, 'hieupc');

INSERT INTO `user_role`
VALUES
    (1, 1),
    (2, 2);

INSERT INTO promotions (id, is_active, discount_value, start_at, end_at, description, discount_type, name)
VALUES
    (1, 1, 10, NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 'Giảm 10k', 'AMOUNT', 'Khuyến mãi tháng 5'),
    (2, 1, 5, NOW(), DATE_ADD(NOW(), INTERVAL 1 WEEK), 'Giảm 5%', 'PERCENTAGE', 'Siêu khuyến mãi tuần cuối'),
    (3, 1, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 'Miễn phí vận chuyển', 'FREE_SHIPPING', 'Free ship tháng 5')
;


INSERT INTO `products` (id, `description`, `name`, `price`, `quantity`, `quantity_sold`, `status`, `unit`, `brand_id`, `category_id`, `promotion_id`, `create_at`, `update_at`)
VALUES
    (1, 'Product 1 description', 'Giay Nike', 1000, 50, 10, 'Active', 'Piece', 1, 1, 1, now(), now()),
    (2, 'Product 2 description', 'Sneaker Adidas', 750, 30, 5, 'Active', 'Piece', 2, 2, 2, now(), now()),
    (3, 'Product 3 description', 'Giay Nike Adidas', 500, 20, 8, 'Active', 'Piece', 1, 2, 3, now(), now()),
    (4, 'Product 4 description', 'Giay Nike Adidas', 500, 20, 8, 'Active', 'Piece', 1, 2, 1, now(), now()),
    (5, 'Product 5 description', 'Boots Puma', 500, 20, 8, 'Active', 'Piece', 3, 3, 2, now(), now()),
    (6, 'Product 6 description', 'Giay The Thao Fila', 500, 20, 8, 'Active', 'Piece', 4, 4, 3, now(), now()),
    (7, 'Product 7 description', 'Giay Cao Got Gucci', 500, 20, 8, 'Active', 'Piece', 5, 5, 1, now(), now());
INSERT INTO `products` (id, `description`, `name`, `price`, `quantity`, `quantity_sold`, `status`, `unit`, `brand_id`, `category_id`, `promotion_id`, `create_at`, `update_at`)
VALUES
    (8, 'Product 8 description', 'Giay Cao Got Gucci 2 ', 500, 10, 8, 'Active', 'Piece', 5, 5, 1, now(), now());

INSERT INTO `product_size` (id, `product_id`, `size_id`, `quantity`, `quantity_sold`)
VALUES
    (1, 1, 1, 10, 0),
    (2, 2, 2, 30, 0),
    (3, 3, 3, 20, 0),
    (4, 4, 1, 20, 0),
    (5, 5, 2, 20, 0),
    (6, 6, 4, 20, 0),
    (7, 7, 5, 20, 0),
    (8, 1, 2, 40, 0);

INSERT INTO `product_images` (id, `url`, `product_id`)
VALUES
    (1,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',1),
    (2,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',2),
    (3,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',3),
    (4,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',4),
    (5,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',5),
    (6,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',6),
    (7,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',7),
    (8,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',1),
    (9,'https://lh3.googleusercontent.com/drive-viewer/AK7aPaA6Sr7132RTiRL_40OS7rJ2v8I4Q0hVAwhX3iWRovb87xfz1UFsdPsr6Z0IUT4pTdtWwGAGrfIz93lGEwoGNfEiC8cP=w1920-h942',2);


INSERT INTO `orders` (id, `discount_amount`, `note`, `order_date`, `payment_method`, `phone_number`, `shipping_address`, `status`, `total_amount`, `user_id`, `voucher_id`)
VALUES
    (1, 0, 'Order note 1', '2023-10-09 14:00:00', 'CASH_ON_DELIVERY', '0983218232', '54 Duy Tân', 'DELIVERED', 75.0, 1, NULL),
    (2, 0, 'Order note 2', '2023-10-09 15:00:00', 'CREDIT_CARD', '0988233721', '55 An Duong Vuong', 'DELIVERED', 15.0, 2, NULL),
    (3, 0, 'Order note 3', '2023-10-09 16:00:00', 'BANK_TRANSFER', '0989233811', '12 Pham Nhu Xuong', 'CANCELLED', 30.0, 2, NULL);

INSERT INTO `order_items` (id, `note`, `quantity`, `unit_price`, `order_id`, `product_id`)
VALUES
    (1, 'Shop giao sớm sớm cho em nhá', 2, 25.0, 1, 1),
    (2, 'Shop nhớ có gì tặng thì bỏ vào giúp em nha hi hi', 3, 30.0, 1, 2),
    (3, '', 1, 15.0, 2, 3),
    (4, '', 1, 30.0, 3, 3);

INSERT INTO comments (id, content, create_at, is_visible, rate, product_id, user_id)
VALUES
    (1, 'Giày này đẹp quá', '2023-10-11 12:00:00.000000', 1, 4, 1, 2),
    (2, 'giày nay không mua uổng phí cuộc đời', '2023-10-11 12:30:00.000000', 1, 5, 1, 1),
    (3, 'Giày này mua đáng đồng tiền bát gạo', '2023-10-11 13:00:00.000000', 1, 3, 2, 1);





INSERT INTO vouchers (ID, `voucher_value`, `discount_type`, `minimum_purchase_amount`, `start_at`, `end_at`, `usage_limit`, `usage_count`, `code`)
VALUES
    (1, 10, 'AMOUNT', 100, now(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 10, 0, 'VOUCHER01'),
    (2, 10, 'FREE_SHIPPING', 100, now(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 10, 0, 'VOUCHER02'),
    (3, 10, 'PERCENTAGE', 100, now(), DATE_ADD(NOW(), INTERVAL 1 MONTH), 10, 0, 'VOUCHER03')
;


-- can sửa type của product price sang double không?



