package com.retail.service;
 
import com.retail.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
 
@Service
@RequiredArgsConstructor
public class EmailService {
 
    private final JavaMailSender mailSender;
 
    @Async
    public void sendOrderConfirmation(String to, String name, Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Order Confirmed #" + order.getId() + " - Retail Store");
            helper.setText(buildOrderEmailHtml(name, order), true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }
 
    private String buildOrderEmailHtml(String name, Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial;max-width:600px;margin:auto;padding:20px;'>");
        sb.append("<h2 style='color:#e63946;'>🎉 Order Confirmed!</h2>");
        sb.append("<p>Hi <strong>").append(name).append("</strong>,</p>");
        sb.append("<p>Your order <strong>#").append(order.getId()).append("</strong> has been placed successfully!</p>");
        sb.append("<table style='width:100%;border-collapse:collapse;'>");
        sb.append("<tr style='background:#f1f1f1;'><th style='padding:8px;text-align:left;'>Item</th><th>Qty</th><th>Price</th></tr>");
        if (order.getItems() != null) {
            for (Object item : order.getItems()) {
                com.retail.entity.OrderItem orderItem = (com.retail.entity.OrderItem) item;
                sb.append("<tr><td style='padding:8px;'>").append(orderItem.getPackaging().getProduct().getName())
                  .append(" (").append(orderItem.getPackaging().getSize()).append(")</td><td style='text-align:center;'>")
                  .append(orderItem.getQuantity()).append("</td><td>₹")
                  .append(orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity()))).append("</td></tr>");
            }
        }
        sb.append("</table>");
        sb.append("<p><strong>Total: ₹").append(order.getTotalAmount()).append("</strong></p>");
        sb.append("<p style='color:#666;'>Delivering to: ").append(order.getDeliveryAddress()).append("</p>");
        sb.append("<p style='color:#2a9d8f;'>🌟 You earned <strong>").append(order.getLoyaltyPointsEarned()).append(" loyalty points</strong>!</p>");
        sb.append("</div>");
        return sb.toString();
    }
}