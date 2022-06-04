module example.com/hello_mod

go 1.18

replace example.com/greetings => ../greetings

require example.com/greetings v0.0.0-00010101000000-000000000000
