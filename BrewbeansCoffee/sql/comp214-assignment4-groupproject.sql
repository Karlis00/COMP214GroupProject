/*COMP214 - SEC.402
  Name: Kit Yi Wan, Yuk Sing Cheung, Kam Hung Chan
  Subject: Assignment 4 - Group Project*/

/*Task 1*/
-- Procedure for updating product description
CREATE OR REPLACE PROCEDURE upd_description_sp (
    p_productid   IN bb_product.idproduct%TYPE,
    p_description IN bb_product.description%TYPE
) IS
BEGIN
    UPDATE bb_product
    SET description = p_description
    WHERE idproduct = p_productid;

    COMMIT;
END upd_description_sp;
-- Testing
SELECT * FROM bb_product;
EXECUTE upd_description_sp(1, 'CapressoBar Model #388');

/*Task 2*/
-- Procedure for adding new product
CREATE OR REPLACE PROCEDURE prod_add_sp (
    p_productname  IN bb_product.productname%TYPE,
    p_description  IN bb_product.description%TYPE,
    p_productimage IN bb_product.productimage%TYPE,
    p_price        IN bb_product.price%TYPE,
    p_active       IN bb_product.active%TYPE
) IS
    v_idproduct bb_product.idproduct%TYPE;
BEGIN
    SELECT MAX(idproduct)
    INTO v_idproduct
    FROM bb_product;

    INSERT INTO bb_product (
        idproduct,
        productname,
        description,
        productimage,
        price,
        active
    ) VALUES (
        v_idproduct + 1,
        p_productname,
        p_description,
        p_productimage,
        p_price,
        p_active
    );

    COMMIT;
END prod_add_sp;
-- Testing
SELECT * FROM bb_product;
EXECUTE prod_add_sp('Roasted Blend', 'Well-balanced mix of roasted beans, a medium body', 'roasted.jpg', 9.50, 1);

/*Task 3*/
-- Procedure for calculating the tax
CREATE OR REPLACE PROCEDURE tax_cost_sp (
    p_state     IN bb_tax.state%TYPE,
    p_subtotal  IN NUMBER,
    p_taxamount OUT NUMBER
) IS
BEGIN
    SELECT p_subtotal * taxrate
    INTO p_taxamount
    FROM bb_tax
    WHERE state = p_state;

    COMMIT;
EXCEPTION
    WHEN no_data_found THEN
        p_taxamount := 0;
END tax_cost_sp;
-- Testing
DECLARE
    v_tax NUMBER;
BEGIN
    tax_cost_sp('VA', 100, v_tax);
    dbms_output.put_line(v_tax);
    tax_cost_sp('ON', 100, v_tax);
    dbms_output.put_line(v_tax);
END;

/*Task 4*/
-- Procedure for updating order status
CREATE OR REPLACE PROCEDURE status_ship_sp (
    p_basketid IN bb_basketstatus.idbasket%TYPE,
    p_date     IN bb_basketstatus.dtstage%TYPE,
    p_shipper  IN bb_basketstatus.shipper%TYPE,
    p_shipnum  IN bb_basketstatus.shippingnum%TYPE
) IS
BEGIN
    INSERT INTO bb_basketstatus (
        idstatus,
        idbasket,
        idstage,
        dtstage,
        shipper,
        shippingnum
    ) VALUES (
        bb_status_seq.NEXTVAL,
        p_basketid,
        3,
        p_date,
        p_shipper,
        p_shipnum
    );

    COMMIT;
END status_ship_sp;
-- Testing
SELECT * FROM bb_basketstatus;
EXECUTE status_ship_sp(3, '20-FEB-12', 'UPS', 'ZW2384YXK4957');

/*Task 5*/
-- Procedure for adding items to a basket
CREATE OR REPLACE PROCEDURE basket_add_sp (
    p_basketid  IN bb_basketitem.idbasket%TYPE,
    p_productid IN bb_basketitem.idproduct%TYPE,
    p_price     IN bb_basketitem.price%TYPE,
    p_quantity  IN bb_basketitem.quantity%TYPE,
    p_size      IN bb_basketitem.option1%TYPE,
    p_form      IN bb_basketitem.option2%TYPE
) IS
BEGIN
    INSERT INTO bb_basketitem (
        idbasketitem,
        idproduct,
        idbasket,
        price,
        quantity,
        option1,
        option2
    ) VALUES (
        bb_idbasketitem_seq.NEXTVAL,
        p_productid,
        p_basketid,
        p_price,
        p_quantity,
        p_size,
        p_form
    );

    COMMIT;
END basket_add_sp;
-- Testing
SELECT * FROM bb_basketitem;
EXECUTE basket_add_sp(14, 8, 10.80, 1, 2, 4);

/*Task 5 - Extended*/
-- Procedure for updating basket
CREATE OR REPLACE PROCEDURE basket_update_sp (
    p_basketid IN bb_basketitem.idbasket%TYPE
) IS
    CURSOR cur_basketitem IS
    SELECT idbasket, price, quantity
    FROM bb_basketitem
    WHERE idbasket = p_basketid;
    v_state    bb_shopper.state%TYPE := '';
    v_quantity bb_basket.quantity%TYPE := 0;
    v_subtotal bb_basket.subtotal%TYPE := 0;
    v_total    bb_basket.total%TYPE := 0;
    v_shipping bb_basket.shipping%TYPE := 0;
    v_tax      bb_basket.tax%TYPE := 0;
BEGIN
    FOR rec_basket IN cur_basketitem LOOP
        v_quantity := v_quantity + rec_basket.quantity;
        v_subtotal := v_subtotal + rec_basket.price;
    END LOOP;

    SELECT fee
    INTO v_shipping
    FROM bb_shipping
    WHERE v_quantity BETWEEN low AND high;

    SELECT state
    INTO v_state
    FROM bb_shopper
        JOIN bb_basket USING ( idshopper )
    WHERE idbasket = p_basketid;

    tax_cost_sp(v_state, v_subtotal, v_tax);
    v_total := v_subtotal + v_shipping + v_tax;
    UPDATE bb_basket
    SET quantity = v_quantity,
        subtotal = v_subtotal,
        total = v_total,
        shipping = v_shipping,
        tax = v_tax
    WHERE idbasket = p_basketid;

    COMMIT;
END basket_update_sp;
-- Testing
SELECT * FROM bb_basket;
SELECT * FROM bb_basketitem;
SELECT * FROM bb_shopper;
SELECT * FROM bb_shipping;
EXECUTE basket_update_sp(14);

/*Task 6*/
-- Function for identifying sale products
CREATE OR REPLACE FUNCTION ck_sale_sf (
    p_productid IN bb_product.idproduct%TYPE,
    p_date      IN bb_product.salestart%TYPE
) RETURN VARCHAR2 IS
    v_start bb_product.salestart%TYPE;
    v_end   bb_product.saleend%TYPE;
BEGIN
    SELECT salestart, saleend
    INTO v_start, v_end
    FROM bb_product
    WHERE idproduct = p_productid;

    IF ( p_date >= v_start AND p_date <= v_end ) THEN
        RETURN 'ON SALE!';
    ELSE
        RETURN 'Great Deal!';
        COMMIT;
    END IF;

END ck_sale_sf;
-- Testing
SELECT * FROM bb_product;
BEGIN
    dbms_output.put_line(ck_sale_sf(6, '10-JUN-12'));
    dbms_output.put_line(ck_sale_sf(6, '19-JUN-12'));
    dbms_output.put_line(ck_sale_sf(5, '10-JUN-12'));
END;

/*Report 1*/
-- Procedure for checking whether all items in stock
CREATE OR REPLACE PROCEDURE ck_instock_sp (
    p_basketid IN bb_basketitem.idbasket%TYPE,
    p_msg OUT VARCHAR2
) IS
    CURSOR cur_basket IS
    SELECT bi.idbasket, bi.quantity, p.stock
    FROM bb_basketitem bi INNER JOIN bb_product p USING ( idproduct )
    WHERE bi.idbasket = p_basketid;
    lv_flag_txt CHAR(1) := 'Y';
BEGIN
    FOR rec_basket IN cur_basket LOOP
        IF rec_basket.stock < rec_basket.quantity THEN
            lv_flag_txt := 'N';
        END IF;
    END LOOP;

    IF lv_flag_txt = 'Y' THEN
        --dbms_output.put_line('All items in stock!');
		p_msg:='All items in stock!';
    ELSIF lv_flag_txt = 'N' THEN
        --dbms_output.put_line('All items NOT in stock!');
		p_msg:='All items NOT in stock!';
    END IF;
    COMMIT;
END ck_instock_sp;
-- Testing
DECLARE
    v_msg VARCHAR2(30);
BEGIN
    ck_instock_sp(6,v_msg);
    dbms_output.put_line(v_msg);
END;

/*Report 2*/
-- Function for calculating total spending
CREATE OR REPLACE FUNCTION tot_purch_sf (
    p_shopperid IN bb_shopper.idshopper%TYPE
) RETURN NUMBER IS
    v_total bb_basket.total%TYPE;
BEGIN
    SELECT SUM(bb_basket.total)
    INTO v_total
    FROM bb_shopper
        FULL OUTER JOIN bb_basket ON bb_shopper.idshopper = bb_basket.idshopper
    WHERE bb_shopper.idshopper = p_shopperid
    AND bb_basket.orderplaced = 1;
    
    IF v_total IS NULL THEN
        v_total:=0;
    END IF;
    
    RETURN v_total;
    COMMIT;
END tot_purch_sf;
-- Full List
SELECT
    bb_shopper.idshopper,
    SUM(bb_basket.total) AS total
FROM
    bb_shopper
    FULL OUTER JOIN bb_basket ON bb_shopper.idshopper = bb_basket.idshopper
GROUP BY
    bb_shopper.idshopper;
-- Testing
SELECT * FROM bb_shopper;
SELECT * FROM bb_basket;
BEGIN
    dbms_output.put_line(tot_purch_sf(21));
    dbms_output.put_line(tot_purch_sf(26));
END;