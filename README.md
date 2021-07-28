# Customer Service
![](https://img.shields.io/badge/jdk-11-blue.svg) ![license](https://img.shields.io/badge/license-MIT-blue.svg) 
![](https://img.shields.io/badge/maven-3.6.3-blue.svg)

This is a Spring Boot application that's used with the "Building a CRM Application " series on the <a href="https://careydevelopment.us" target="_blank">Carey Development website</a>.

It's a microservice that handles requests related to customers (including contacts and accoutns) within an ecosystem.

Each branch within this repo is related to a distinct guide. The master branch holds the latest version of the application.

If you want to follow along with the series, just visit the URL that points to the <a href="https://careydevelopment.us/tag/careydevelopmentcrm" target="_blank">careydevelopmentcrm tag</a>. 

Remember, all guides are in reverse chronological order so if you want to start from the beginning, you'll need to go to the last page.

## Configuration
Bad news: you can't just clone this source and run it right out of the box. You'll need to make some changes.

Here's a list of the properties you need to set in the `application.properties` file:
* `jwt.secret` - the secret used to sign JWTs
* `mongodb.carey-crm.connection` - the connection string to get to the MongoDB (in the format: mongodb://name:password@server:port)
* `ecosystem-user-service.endpoint` - the base URL to <a href="https://github.com/careydevelopment/ecosystem-user-service">the user service</a>
* `customer-service.endpoint` - the base URL to <a href="https://github.com/careydevelopment/ecosystem-customer-service">the customer service.</a>
* `geo-service.endpoint` - the base URL to state and country lookups (currently private repo).
* `product-service.endpoint` - the base URL to <a href="https://github.com/careydevelopment/ecosystem-product-service">the product service.</a>
* `crm-service.endpoint` - the base URL to <a href="https://github.com/careydevelopment/crm-service">the CRM service.</a> 
* `ip.whitelist` - comma-separated list of IP addresses allowed to access the service 

If you're deploying to Kubernetes, you could also store those properties in an external config file as I describe <a href="https://careydevelopment.us/blog/spring-boot-and-kubernetes-how-to-use-an-external-json-configuration" target="_blank">here</a>.

## Dependencies

This service uses <a href="https://github.com/careydevelopment/ecosystem-user-service">ecoystem-user-service</a> to get user information. You'll
need to deploy that service if you want to persist customer-related details. 

## The UI
The Carey Development CRM <a href="https://github.com/careydevelopment/careydevelopmentcrm">source</a> uses this service.

## License
This code is under the [MIT License](https://github.com/careydevelopment/ecosystem-customer-service/blob/master/LICENSE).