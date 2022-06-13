#!/bin/bash


sprint=$1
# array of all the features
features=()
root="$(dirname "$0")/../deca"
if [ -d "${root}/syntax/invalid/${sprint}" ]; then
    for feature in "${root}/syntax/invalid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi
if [ -d "${root}/syntax/valid/${sprint}" ]; then
    for feature in "${root}/syntax/valid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi

if [ -d "${root}/context/invalid/${sprint}" ]; then
    for feature in "${root}/context/invalid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi
if [ -d "${root}/context/valid/${sprint}" ]; then
    for feature in "${root}/context/valid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi
if [ -d "${root}/codegen/invalid/${sprint}" ]; then
    for feature in "${root}/codegen/invalid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi
if [ -d "${root}/codegen/valid/${sprint}" ]; then
    for feature in "${root}/codegen/valid/${sprint}"/* ; do
        if [ -d "${feature}" ]; then
            # if the feature is not in the array, add it
            if ! [[ "${features[@]}" =~ $(basename "${feature}") ]]; then
                features+=("$(basename "${feature}")")
            fi
        fi
    done
fi
# if the array is empty, we presume that the sprint is the feature
if [ ${#features[@]} -eq 0 ]; then
    "$(dirname "$0")/testLauncher.sh" "${sprint}" "decompile" || exit 1
    exit 0
fi

for feature in "${features[@]}" ; do
    "$(dirname "$0")/testLauncher.sh" "${sprint}/${feature}" "decompile" || exit 1
done