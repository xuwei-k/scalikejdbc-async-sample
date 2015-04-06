# scalikejdbc-async-sample

実験コードです。

Company.createは10回に１回失敗します。ここでrollbackしてほしいけど、Actorを使うとされません。

Actorを使わないと正しくrollbackされます。
