# Limpar o diretório bin antes de compilar
Remove-Item -Recurse -Force bin\*

# Compilar arquivos .java para o diretório bin
javac -d bin -cp bin -sourcepath src $(Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName })

# Verificar se a compilação foi bem-sucedida
if ($?) {

    Write-Host "Compilação bem-sucedida!" -BackgroundColor Green -ForegroundColor White

    # Executar o programa Java
    java -cp bin pt.iscte.poo.game.Main $args
} else {

    Write-Host "Erro na compilação." -BackgroundColor Red -ForegroundColor White

}

