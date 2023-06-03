#!/usr/bin/env bash
set -euo pipefail -o errtrace

#sh release-script.sh 0.4.0 0.5.0 <token>

REPO_NAME="${PWD##*/}"

declare RELEASE_VERSION
declare NEXT_VERSION
declare TOKEN

function readParams() {
    RELEASE_VERSION="$1"
    NEXT_VERSION="$2"
#    TOKEN="$3"
}

function checkVersion {
    local version="$1"
    if [[ -z "$version" ]]; then
        echo "Version must be set" 1>&2
        exit 1
    fi

    if [[ ! "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        echo "Invalid version format: $version" 1>&2
        exit 1
    fi
}

function createAndPushBranch {
    local -r branchName="$1"
    local -r version="$2"
    local -r commitMsg="$3"
    local -r shouldTagCommit="$4"

    echo "Create branch: $branchName"
    git checkout -b "$branchName"

    echo "Set maven version to $version"
    mvn versions:set "-DnewVersion=$version" -DgenerateBackupPoms=false

    git commit -a -m "$commitMsg"
    if [ "$shouldTagCommit" = true ] ; then
      echo "Tagging commit"
      git tag -a "$version" -m "Release $version"
    fi

    echo "Push to repo"
    git push -u --follow-tags origin "$branchName"
}

#function createPR {
#    local -r sourceBranch="$1"
#    local -r targetBranch="$2"
#    local -r title="$3"
#
#    url="https://bitbucket.org/api/2.0/repositories/konradboniecki/$REPO_NAME/pullrequests"
#    body='{"title": "'$title'", "source":{"branch":{"name": "'$sourceBranch'"}}, "destination":{"branch":{"name":"'$targetBranch'"}}}'
#    echo $url
#    echo $body
#    curl -X POST "$url" -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d "$body"
#
#    echo "Create PR: $title"
#    echo
#}

function preparePR {
    local -r sourceBranch="$1"
    local -r targetBranch="$2"
    local -r version="$3"
    local -r title="$4"
    local -r commitMsg="$5"
    local -r shouldTagCommit="$6"

    createAndPushBranch "$sourceBranch" "$version" "$commitMsg" "$shouldTagCommit"
#    createPR "$sourceBranch" "$targetBranch" "$title"
 }

readParams "$@"
checkVersion "$RELEASE_VERSION"
checkVersion "$NEXT_VERSION"

echo "=================== Get $REPO_NAME develop branch ==================="
git checkout develop
git pull

#preparePR <sourceBranch> <targetBranch> <version> <prTitle> <commitMsg> <shouldTagCommit>

echo "=================== Prepare $REPO_NAME release $RELEASE_VERSION ==================="
preparePR "release/${RELEASE_VERSION}" master "$RELEASE_VERSION" \
"Release $REPO_NAME $RELEASE_VERSION" "Release $REPO_NAME $RELEASE_VERSION" true
echo

echo "=================== Prepare $REPO_NAME next release $NEXT_VERSION-SNAPSHOT =================="
preparePR "release/${RELEASE_VERSION}_develop" develop "$NEXT_VERSION-SNAPSHOT" \
"Prepare next release $REPO_NAME $NEXT_VERSION-SNAPSHOT" "Prepare next release $REPO_NAME $NEXT_VERSION-SNAPSHOT" false
echo
