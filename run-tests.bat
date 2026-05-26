@echo off
cd /d "c:\Desenvolvimento\projetos\simulador-financiamentos"
call mvnw.cmd clean test jacoco:report -Pcoverage > test-results.txt 2>&1
echo Tests completed
