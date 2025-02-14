DROP TABLE IF EXISTS member;
CREATE TABLE member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 아이디',
    user_pw VARCHAR(255) NOT NULL COMMENT '사용자 비밀번호',
    user_name VARCHAR(10) NOT NULL COMMENT '사용자 이름',
    user_telno VARCHAR(20) DEFAULT NULL COMMENT '사용자 전화번호',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    UNIQUE (user_id)
);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL COMMENT '등록자 아이디',
    product_name VARCHAR(100) NOT NULL COMMENT '상품 이름',
    product_description VARCHAR(255) DEFAULT NULL COMMENT '상품 설명',
    price DECIMAL(10, 2) NOT NULL COMMENT '상품 가격',
    sales_status int not null default 1 comment '판매 상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS chat_room;
CREATE TABLE chat_room (
    chat_room_id UUID PRIMARY KEY COMMENT '채팅방 아이디(UUID)',
    product_id BIGINT NOT NULL COMMENT '상품 아이디',
    buyer_id VARCHAR(50) NOT NULL COMMENT '구매자 아이디',
    seller_id VARCHAR(50) NOT NULL COMMENT '판매자 아이디',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);

DROP TABLE IF EXISTS chat;
CREATE TABLE chat (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '채팅 ID',
    chat_room_id UUID NOT NULL COMMENT '채팅방 아이디',
    product_id BIGINT NOT NULL COMMENT '상품 아이디',
    send_user_id VARCHAR(50) NOT NULL COMMENT '발신자 아이디',
    receive_user_id VARCHAR(50) NOT NULL COMMENT '수신자 아이디',
    message VARCHAR(500) NOT NULL COMMENT '채팅 메세지',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    CONSTRAINT fk_chat_chatroom FOREIGN KEY (chat_room_id) REFERENCES chat_room (chat_room_id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payment_id UUID NOT NULL UNIQUE COMMENT '주문 ID',
    product_id BIGINT NOT NULL COMMENT '상품 정보',
    buyer_id VARCHAR(50) NOT NULL COMMENT '구매자 정보',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);

CREATE TABLE payment (
    id UUID PRIMARY KEY COMMENT '결제 ID (UUID)',
    m_id VARCHAR(255) NOT NULL COMMENT '가맹점 ID',
    payment_key VARCHAR(255) NOT NULL COMMENT '결제 고유 키',
    order_id VARCHAR(255) NOT NULL COMMENT '주문 ID',
    order_name VARCHAR(255) NOT NULL COMMENT '구매상품명',
    status VARCHAR(50) NOT NULL COMMENT '결제 처리 상태',
    requested_at TIMESTAMP NOT NULL COMMENT '결제 요청 일시',
    approved_at TIMESTAMP DEFAULT NULL COMMENT '결제 승인 일시',
    total_amount INT NOT NULL COMMENT '총 결제 금액',
    account_number VARCHAR(50) NOT NULL COMMENT '가상계좌번호',
    bank_code VARCHAR(10) NOT NULL COMMENT '은행 코드',
    customer_name VARCHAR(255) NOT NULL COMMENT '예금주명',
    due_date TIMESTAMP NOT NULL COMMENT '입금 만료 일시',
    expired BOOLEAN NOT NULL COMMENT '입금 만료 여부',
    settlement_status VARCHAR(50) NOT NULL COMMENT '결제 상태',
    refund_status VARCHAR(50) NOT NULL COMMENT '환불 처리 상태',
    refund_bank_code VARCHAR(10) DEFAULT NULL COMMENT '환불 은행 코드',
    refund_account_number VARCHAR(50) DEFAULT NULL COMMENT '환불 계좌번호',
    refund_holder_name VARCHAR(255) DEFAULT NULL COMMENT '환불 예금주명',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);

