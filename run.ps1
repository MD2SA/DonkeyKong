# Checks if bin folder (folder with compiled files) exists
if (Test-Path -Path "bin") {
    # Cleans it
    Remove-Item -Recurse -Force bin\*
}

# Compiles files into bin folder, if doesn't exist creates it
javac -d bin -cp bin -sourcepath src $(Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName })

# Checks if previous commands had errors
if ($?) {

    Write-Host "Successful Compilation" -ForegroundColor Green

    # Execute java project with args of this script
    java -cp bin pt.iscte.poo.game.Main $args
} else {

    Write-Host "Compilation Error" -ForegroundColor Red

}

