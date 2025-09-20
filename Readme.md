# E-Commerce Service-Oriented Monolithic Application

This repository contains the service-oriented monolithic architecture for an e-commerce platform.  
It includes multiple domains such as User, Client, Merchant, Cart, Product, Order, Payment, Notification, Media, and Admin Management.

---

## Features

- Multi-role users (Client, Merchant, or both)
- Redis caching for Cart, Products, Sessions, and Store Config
- PostgreSQL as the primary relational database
- Integration with external services:
  - AWS Cognito (User Authentication)
  - Stripe (Payment Processing)
  - Twilio / SendGrid (Notifications)
  - AWS S3 / Object Storage (Media)
- AbstractService layer for shared contracts and business logic
- Domain-layered architecture for maintainability and future scalability

---

## Diagram

The architecture is visualized in Mermaid below. You can find a pre-rendered image of the diagram in the root directory as well (`![component_architecture.png](component_architecture.png)`).

```mermaid
flowchart TB
%% External clients
C1["Merchant Dashboard"] -->|HTTPS| LB["Load Balancer"]
C2["Customer Mobile/Web"] -->|HTTPS| LB
C3["Admin Panel"] -->|HTTPS| LB

%% Load balancer to application instances
LB --> APP1["Service Instance 1"]
LB --> APP2["Service Instance 2"]
LB --> APP3["Service Instance N"]

%% Service-Oriented Application
subgraph "Service-Oriented Application"
    %% Facade Layer
    subgraph "Facade Layer"
        SHOPFACADE["ShoppingExperienceFacade"]
        MERCHANTFACADE["MerchantExperienceFacade"]
        ADMINFACADE["AdminExperienceFacade"]
    end

    %% API Layer
    subgraph "API Layer"
        REST["REST Controllers"]
        AUTH["Authentication"]
        VALID["Validation"]
    end

    %% Service Layer
    subgraph "Service Layer"
        STORE["Store Service"]
        PRODUCT["Product Service"]
        PRODUCTCATALOG["Product Catalog Service"]
        MERCHANT["Merchant Service"]
        USER["User Service (Cognito Integration)"]
        MEDIA["Media Resource Service"]
        ORDER["Order Service"]
        PAYMENT["Payment Service"]
        NOTIFY["Notification Service"]
        APPOINTMENT["Appointment Service"]
        CART["Cart Service using Redis"]
        DASHBOARD["Dashboard/Analytics Service"]
        ADMINMGMT["Admin Management Service"]
    end

    %% Abstract Service Layer
    subgraph "Abstract Service Layer"
        ABSTRACT["AbstractService - Shared Contracts & Interfaces"]
    end

    %% Repository Layer
    subgraph "Repository Layer"
        STORE_REPO["StoreRepository"]
        PRODUCT_REPO["ProductRepository"]
        PRODUCTCATALOG_REPO["ProductCatalogRepository"]
        MERCHANT_REPO["MerchantRepository"]
        USER_REPO["UserRepository"]
        MEDIA_REPO["MediaResourceRepository"]
        ORDER_REPO["OrderRepository"]
        APPOINTMENT_REPO["AppointmentRepository"]
        ADMIN_REPO["AdminRepository"]
    end

    %% Connections
    REST --> SHOPFACADE & MERCHANTFACADE & ADMINFACADE
    SHOPFACADE --> PRODUCT & CART & ORDER & PAYMENT
    MERCHANTFACADE --> PRODUCT & STORE & PRODUCTCATALOG & DASHBOARD
    ADMINFACADE --> MERCHANT & ADMINMGMT & USER & ORDER
    PRODUCT & STORE & PRODUCTCATALOG & MERCHANT & USER & MEDIA & ORDER & PAYMENT & NOTIFY & APPOINTMENT & CART & DASHBOARD & ADMINMGMT --> ABSTRACT
    ABSTRACT --> STORE_REPO & PRODUCT_REPO & PRODUCTCATALOG_REPO & MERCHANT_REPO & USER_REPO & MEDIA_REPO & ORDER_REPO & APPOINTMENT_REPO & ADMIN_REPO
end

%% External integrations
APP1 --> CACHE["Redis Cache Cluster"]
APP2 --> CACHE
APP3 --> CACHE

USER -->|Cognito| COGNITO["AWS Cognito"]
PAYMENT -->|HTTPS| STRIPE["Stripe API"]
NOTIFY -->|HTTPS| SENDGRID["SendGrid API"]
NOTIFY -->|HTTPS| TWILIO["Twilio API"]
MEDIA -->|HTTPS| S3["AWS S3 / Object Storage"]

%% Database Schema
subgraph "Database Schema"
    USERS["Users Table (id, cognito_uuid, email, role)"]
    CLIENTS["Clients Table (id, user_id FK)"]
    MERCHANTS["Merchants Table (id, user_id FK)"]
    STORES["Stores Table"]
    PRODUCTS["Products Table"]
    PRODUCT_CATALOGS["Product Catalog Table"]
    MEDIA_RESOURCES["Media Resources Table"]
    ORDERS["Orders Table"]
    APPOINTMENTS["Appointments Table"]
    CATEGORIES["Categories Table"]
    ADMINS["Admins Table"]
end

DB --> USERS & CLIENTS & MERCHANTS & STORES & PRODUCTS & PRODUCT_CATALOGS & MEDIA_RESOURCES & ORDERS & APPOINTMENTS & CATEGORIES & ADMINS
USERS --> CLIENTS
USERS --> MERCHANTS

%% Redis cache structure
subgraph "Redis Cache"
    PROD_CACHE["Product Cache"]
    CART_CACHE["Cart Cache TTL 15m"]
    SESSION_CACHE["Session Cache TTL 30m"]
    STORE_CACHE["Store Config Cache TTL 1h"]
end
CART --> CART_CACHE
CACHE --> PROD_CACHE & CART_CACHE & SESSION_CACHE & STORE_CACHE
