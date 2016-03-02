<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String username = request.getParameter("userid");
    String kredit = request.getParameter("kredit");

%>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="format-detection" content="telephone=no" />
        <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=160" />
    </head>
    <body>
        <!-- INFO: The post URL "checkout.java" is invoked when clicked on "Pay with PayPal" button.-->

        <h1>Platba PayPalem</h1>
        <p>Stránka bude v grafice hry</p>


        <form action='checkout.java' METHOD='POST' name='f1'>
            Uživatel: <input type="text" name="username" value="<%=username%>" /><br />
            Kredit: <input type="text" name="kredit" value="<%=kredit%>" />EUR
            <input type='submit' name='paypal_submit' id='paypal_submit' value="Koupit" border='0' align='top' alt='Pay with PayPal'/>
        </form>




        <!-- Add Digital goods in-context experience. Ensure that this script is added before the closing of html body tag -->

        <script src='https://www.paypalobjects.com/js/external/dg.js' type='text/javascript'></script>


        <script>
            var dg = new PAYPAL.apps.DGFlow(
                    {
                        trigger: 'paypal_submit',
                        expType: 'instant'
                                //PayPal will decide the experience type for the buyer based on his/her 'Remember me on your computer' option.
                    });
        </script>

    </body>
</html>

