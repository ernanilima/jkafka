<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <title>Email de verificação</title>

    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }

        #header, #content, #footer {
            padding: 10px;
        }

        #header {
            background: darkblue;
            color: white;
        }

        #content {
            background: aliceblue;
        }

        #footer {
            background-color: white;
            color: lightgray;
        }

        .table-body {
            margin: auto;
            border: 0;
            width: 600px;
            border-collapse: collapse;
        }

        .security-link, .security-code {
            color: darkblue;
        }

        .security-code {
            font-size: 1.5em;
        }

        .link-copy-paste {
            font-size: 0.8em;
        }
    </style>
</head>
<body style="background-color: #efefef; padding-top: 10px; padding-bottom: 10px">
<table class="table-body">
    <tr>
        <td id="header">
            <h2>Email de verificação para ${application}</h2>
        </td>
    </tr>
    <tr>
        <td id="content">
            <p>
                Olá,
                <br>
                Seu código de verificação:
            </p>
            <strong class="security-code">${securityCode}</strong>
            <p>
                O código de verificação será válido por alguns minutos.
                <br>
                Por favor, não compartilhe este código com ninguém.
            </p>
            <p>
                <strong><a class="security-link" href="${securityLink}">Click aqui</a></strong> para prosseguir com a
                verificação do seu e-mail.
            </p>

            <br>

            <p class="link-copy-paste">
                Link para copiar/colar: ${securityLink}
            </p>
        </td>
    </tr>
    <tr>
        <td id="footer">
            <p>
                Mensagem automatizada. Por favor, não responda.
            </p>
        </td>
    </tr>
</table>
</body>
</html>
