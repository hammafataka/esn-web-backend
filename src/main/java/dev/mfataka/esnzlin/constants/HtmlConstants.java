package dev.mfataka.esnzlin.constants;

import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.experimental.UtilityClass;

import dev.mfataka.esnzlin.jpa.domain.Event;
import dev.mfataka.esnzlin.service.ImageService;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:34
 */
@UtilityClass
public class HtmlConstants {
    public static final String EMAIL_VERIFICATION_TEMPLATE = """
            <!DOCTYPE html>
                   <html>
                   <head>
                       <meta charset="UTF-8">
                       <meta name="viewport" content="width=device-width, initial-scale=1.0">
                       <title>Email Verification</title>
                       <style>
                           body {
                               font-family: 'Arial', sans-serif;
                               background-color: #f4f4f7;
                               margin: 0;
                               padding: 0;
                               -webkit-text-size-adjust: none;
                               color: #51545E;
                           }
                           .email-wrapper {
                               width: 100%;
                               background-color: #f4f4f7;
                               padding: 20px;
                           }
                           .email-content {
                               max-width: 600px;
                               margin: 0 auto;
                               background-color: #ffffff;
                               border-radius: 8px;
                               padding: 40px;
                               text-align: center;
                               box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                           }
                           .email-header {
                               font-size: 24px;
                               font-weight: bold;
                               color: #333333;
                               margin-bottom: 20px;
                           }
                           .email-body {
                               font-size: 16px;
                               color: #51545E;
                               margin-bottom: 30px;
                           }
                           .email-body p {
                               margin: 0 0 20px;
                           }
                           .email-button {
                               display: inline-block;
                               padding: 12px 24px;
                               font-size: 16px;
                               color: #ffffff;
                               background-color: #3869D4;
                               border-radius: 4px;
                               text-decoration: none;
                               font-weight: bold;
                               transition: background-color 0.3s ease;
                           }
                           .email-button:hover {
                               background-color: #2A4B99;
                           }
                           .email-footer {
                               font-size: 12px;
                               color: #A8AAAF;
                               margin-top: 40px;
                           }
                           .email-footer p {
                               margin: 0;
                               line-height: 1.5;
                           }
                           .email-footer a {
                               color: #3869D4;
                               text-decoration: none;
                           }
                       </style>
                   </head>
                   <body>
                   <div class="email-wrapper">
                       <div class="email-content">
                           <div class="email-header">
                               Verify Your Email Address
                           </div>
                           <div class="email-body">
                               <p>Hello,</p>
                               <p>Thank you for registering with us! Please confirm your email address by clicking the button below.</p>
                               <a href='verificationLink' class="email-button">Verify Email</a>
                               <p>If you did not create an account, no further action is required.</p>
                           </div>
                           <div class="email-footer">
                               <p>If you’re having trouble clicking the "Verify Email" button, copy and paste the URL below into your web browser:</p>
                               <p><a href='verificationLink'>verificationLink</a></p>
                               <p>Thank you for choosing our service!</p>
                           </div>
                       </div>
                   </div>
                   </body>
                   </html>
                    """;


    private final static String successVerificationHtml =
            """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Email Verification Success</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #e0f7fa;
                                margin: 0;
                                padding: 0;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                height: 100vh;
                                text-align: center;
                                color: #00796b;
                            }
                            .container {
                                max-width: 600px;
                                padding: 20px;
                                background-color: #ffffff;
                                border-radius: 8px;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                                animation: fadeOut 5s forwards;
                            }
                            .container h1 {
                                font-size: 32px;
                                margin-bottom: 20px;
                            }
                            .container p {
                                font-size: 18px;
                                margin-bottom: 40px;
                            }
                            @keyframes fadeOut {
                                0% { opacity: 1; }
                                90% { opacity: 1; }
                                100% { opacity: 0; }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1>Verification Successful!</h1>
                            <p>Your email has been successfully verified. You will be redirected shortly.</p>
                        </div>
                        <script>
                            setTimeout(function() {
                                window.close();
                                window.history.back();
                            }, time_out);
                        </script>
                    </body>
                    </html>
                    """;

    private final static String failedVerificationHtml =
            """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Email Verification Failed</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #ffebee;
                                margin: 0;
                                padding: 0;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                height: 100vh;
                                text-align: center;
                                color: #d32f2f;
                            }
                            .container {
                                max-width: 600px;
                                padding: 20px;
                                background-color: #ffffff;
                                border-radius: 8px;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                            }
                            .container h1 {
                                font-size: 32px;
                                margin-bottom: 20px;
                            }
                            .container p {
                                font-size: 18px;
                                margin-bottom: 20px;
                            }
                        </style>
                    </head>
                    <body>
                    <div class="container">
                        <h1>Verification Failed</h1>
                        <p>Error_Message. Please try again or contact support.</p>
                    </div>
                    </body>
                    </html>
                    """;


    private final static String paymentConfirmStyle = """
                                    <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                margin: 0;
                                padding: 0;
                            }
                            .email-container {
                                max-width: 600px;
                                margin: 20px auto;
                                background: #ffffff;
                                padding: 20px;
                                border-radius: 8px;
                                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                            }
                            .header {
                                text-align: center;
                                padding: 10px 0;
                            }
                            .header h1 {
                                color: #333;
                                margin: 0;
                            }
                            .content {
                                margin-top: 20px;
                            }
                            .content p {
                                color: #555;
                                font-size: 16px;
                                line-height: 1.5;
                            }
                            .items-table {
                                width: 100%;
                                border-collapse: collapse;
                                margin-top: 20px;
                            }
                            .items-table th, .items-table td {
                                text-align: left;
                                padding: 10px;
                                border: 1px solid #ddd;
                            }
                            .items-table th {
                                background-color: #f8f8f8;
                                font-weight: bold;
                            }
                            .item-image {
                                width: 80px;
                                height: auto;
                                border-radius: 5px;
                            }
                            .footer {
                                text-align: center;
                                margin-top: 30px;
                                padding-top: 10px;
                                border-top: 1px solid #ddd;
                            }
                            .footer p {
                                font-size: 14px;
                                color: #999;
                            }
                        </style>
            """;
    private final static String paymentConfirmationHtml =
            """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Payment Confirmation</title>
                        %s
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <h1>Payment Confirmation</h1>
                            </div>
                            <div class="content">
                                <p>Dear %s,</p>
                                <p>Thank you for your payment! We have successfully received your payment for the following items:</p>
                                        
                                <p><strong>Payment No:</strong> %d</p>
                                        
                                <table class="items-table">
                                    <thead>
                                        <tr>
                                            <th>Image</th>
                                            <th>Title</th>
                                            <th>Description</th>
                                            <th>Price</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        %s
                                    </tbody>
                                </table>
                                        
                                <p><strong>Total Paid:</strong> %d</p>
                            </div>
                            <div class="footer">
                                <p>If you have any questions, feel free to contact us at wpa@esnzlin.cz.</p>
                                <p>Thank you for choosing ESN Zlin!</p>
                            </div>
                        </div>
                    </body>
                    </html>
                                        
                    """;
    private final static String paymentItemHtml =
            """
                                                    <tr>
                                                        <td><img src="%s" alt="Item Image" class="item-image"></td>
                                                        <td>%s</td>
                                                        <td>%s</td>
                                                        <td>%d</td>
                                                    </tr>
                    """;

    public static String getPaymentItemHtml(final String imageUrl, final String title, final String description, final long price) {
        return String.format(paymentItemHtml, imageUrl, title, description, price);
    }


    public static Mono<String> getPaymentConfirmationHtml(final String customerName,
                                                          final long paymentNumber,
                                                          final ImageService imageService,
                                                          final List<Event> items,
                                                          final long totalAmount) {
        return Flux.fromIterable(items)
                .flatMap(event -> event.asPaymentItemHtml(imageService))
                .collect(Collectors.joining(""))
                .map(itemTags -> String.format(paymentConfirmationHtml, paymentConfirmStyle, customerName, paymentNumber, itemTags, totalAmount));

    }

    public static String getFailedVerificationHtml(final String failDisplayMessage) {
        return failedVerificationHtml.replace("Error_Message.", failDisplayMessage);
    }

    public static String getSuccessVerificationHtml(final int timeoutMillis) {
        return successVerificationHtml.replace("time_out", String.valueOf(timeoutMillis));
    }

    public static String getEmailVerificationHtml(final String url) {
        return EMAIL_VERIFICATION_TEMPLATE.replace("verificationLink", url);

    }
}
