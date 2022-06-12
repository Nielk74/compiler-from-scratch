#!/bin/bash

# This script tests the decac options.
# -P for parallelism compilation
# -r <X> X between 4 and 16 for the number of registers

# The script tests parallelism feature with
# every codegen tests.

# The script tests the number of registers inside
# .ass files.

# The script assumes that every .deca file can be compiled

echo "### TEST: decac options ###"

echo "### TEST: -P parallelism ###"
nbtests=0
nbpassed=0

root="$(dirname "$0")/../deca/codegen"
tmpDir="$(dirname "$0")/../deca/tmp"

# copy deca files to tmp directory and add an index to the name in order to avoid name collision
index=0
getCodegenFiles () {
    mkdir $tmpDir || exit 1
    find ${root} -name "*.deca" ! -name '*_decompiled.deca' | grep -v 'provided' | grep -v 'interactive' |while read f; do
        file="${f%.deca}"
        cp "${f}" "${tmpDir}/$(basename $file)_${index}.deca"
        realPath="$(realpath "${file}.ass")"
        ln -s "$realPath" "${tmpDir}/$(basename $file)_${index}.link"
        index=$((index+1))
    done
}

clearTmpDir(){
    rm ${tmpDir}/*
    rmdir ${tmpDir}
}

getCodegenFiles
decac -P "${tmpDir}"/*.deca

for f in ${tmpDir}/*.deca ; do
    file="${f%.deca}"
    nbtests=$((nbtests+1))
    # get the base name of the file
    base="$(basename "${f%.deca}")"
    # remove after the last _
    base="${base%_*}"
    # basePath="$(find "${root}" -name "${base}.ass")"
    basePath="${file}.link"
    brutPath=$(realpath "${basePath}")
    if diff -q "${file}.ass" "${basePath}" > /dev/null ; then
        echo "--- ${brutPath#*deca/}: PASSED ---"
        nbpassed=$((nbpassed+1))
    else
        echo "--- ${brutPath#*deca/}: FAILED ---"
        diff "${file}.ass" $basePath
    fi
    echo 
done
echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"

clearTmpDir

nbpassed=0
nbtests=0
echo "### TEST: -r limited registers ###"

getCodegenFiles

decac -P -r 4 "${tmpDir}"/*.deca

for f in ${tmpDir}/*.deca ; do
    file="${f%.deca}"
    nbtests=$((nbtests+1))
    # get the base name of the file
    base="$(basename "${f%.deca}")"
    # remove after the last _
    base="${base%_*}"
    # basePath="$(find "${root}" -name "${base}.ass")"
    basePath="${file}.link"
    brutPath=$(realpath "${basePath}")
    # if the number of registers is less than 4, the file is OK
    register=0
    error=0
    for (( i=4; i<=16; i++ )) ; do
        if grep -q "R${i}" "${file}.ass" ; then
            error=1
            register=$i
            break
        fi
    done
    # if $error = 0
    if [ $error -eq 0 ]; then
        echo "--- ${brutPath#*deca/}: PASSED ---"
        nbpassed=$((nbpassed+1))
    else
        echo "--- ${brutPath#*deca/}: FAILED ---"
        echo "The script use ${register} registers instead of less than 4"
    fi
    echo 
done

echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"
clearTmpDir

decac_help="$(decac -h)"
if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -h a termine avec un status different de zero."
    exit 1
fi
if [ "$decac_help" = "" ]; then
    echo "ERREUR: decac -h n'a produit aucune sortie"
    exit 1
fi
if echo "$decac_help" | grep -i -e "erreur" -e "error"; then
    echo "ERREUR: La sortie de decac -h contient erreur ou error"
    exit 1
fi
echo "Pas de problème détecté avec decac -h."

decac_moins_v_p=$(decac -v -p > /dev/null 2>&1)

if [ "$?" -ne 1 ]; then
    echo "ERREUR: decac -v -p a terminé avec un status different de 1."
    exit 1
else
    echo "decac -v -p retourne bien une erreur."
fi


decac_moins_p_v=$(decac -p -v > /dev/null 2>&1)
if [ "$?" -ne 1 ]; then
    echo "ERREUR: decac -p -v a terminé avec un status different de 1."
    exit 1
else
    echo "decac -p -v retourne bien une erreur."
fi

decac_file_not_found=$(decac file_not_found.deca > /dev/null 2>&1)
if [ "$?" -ne 1 ]; then
    echo "ERREUR: decac file_not_found.deca a terminé avec un status different de 1."
    exit 1
else
    echo "decac file_not_found.deca retourne bien une erreur."
fi

decac_moins_r=$(decac -r)
if [ "$?" -ne 1 ]; then
    echo "ERREUR: decac -r sans argument a terminé avec un status different de 1."
    exit 1
else
    echo "decac -r sans argument retourne bien une erreur."
fi

decac_moins_r_out_of_range=$(decac -r 17)
if [ "$?" -ne 1 ]; then
    echo "ERREUR: decac -r 17 a terminé avec un status different de 1."
    exit 1
else
    echo "decac -r 17 retourne bien une erreur."
fi
