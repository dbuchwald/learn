{{/*
Expand the name of the chart.
*/}}
{{- define "api-server.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "api-server.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create a database fully qualified name based on the app name
*/}}
{{- define "api-database.fullname" -}}
{{- $appName := include "api-server.fullname" . | trunc 60 | trimSuffix "-" }}
{{- printf "%s-db" $appName }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "api-server.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "api-server.labels" -}}
helm.sh/chart: {{ include "api-server.chart" . }}
{{ include "api-server.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "api-server.selectorLabels" -}}
app.kubernetes.io/name: {{ include "api-server.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "api-server.serviceAccountName" -}}
  {{- if .Values.serviceAccount.create }}
    {{- default (include "api-server.fullname" .) .Values.serviceAccount.name }}
  {{- else }}
    {{- default "default" .Values.serviceAccount.name }}
  {{- end }}
{{- end }}

{{/*
Create profiles template
*/}}
{{- define "api-server.runtime.profiles" -}}
  {{- if and (eq .Values.database.provider "derby") (not .Values.database.embedded) }}
    {{- print "derbyclient" }}
  {{- else }}
    {{- .Values.database.provider }}
  {{- end }}
  {{- if .Values.test.enabled }}
    {{- print ",test" }}
  {{- end }}
{{- end }}

{{/*
Create database hostname
*/}}
{{- define "api-server.database.hostname" -}}
{{- include "api-database.fullname" . }}
{{- end }}

{{/*
Create database URL
*/}}
{{- define "api-server.database.url" -}}
  {{- if not .Values.database.embedded }}
    {{- printf "//%s:%d/%s" (include "api-server.database.hostname" .) (include "api-server.database.port" . | int) .Values.database.databaseName }}
  {{- else }}
    {{- if eq .Values.database.provider "mariadb" }}
      {{- fail "MariaDB provider doesn't support embedded database" }}
    {{- else }}
      {{- if eq .Values.database.provider "derby" }}
        {{- printf "/var/apidb/%s;create=true" .Values.database.databaseName }}
      {{- else }}
        {{- if eq .Values.database.provider "h2" }}
          {{- printf "/var/apidb/%s;DB_CLOSE_DELAY=-1" .Values.database.databaseName }}
        {{- else }}
          {{- fail "Unsupported database provider" }}
        {{- end }}
      {{- end }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create database image
*/}}
{{- define "api-server.database.image" -}}
  {{- if .Values.database.imageOverride }}
    {{- .Values.database.imageOverride }}
  {{- else }}
    {{- if eq .Values.database.provider "mariadb" }}
      {{- .Values.database.defaults.mariadb.image }}
    {{- end }}
    {{- if eq .Values.database.provider "derby" }}
      {{- .Values.database.defaults.derby.image }}
    {{- end }}
    {{- if eq .Values.database.provider "h2" }}
      {{- .Values.database.defaults.h2.image }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create database port
*/}}
{{- define "api-server.database.port" -}}
  {{- if .Values.database.portOverride }}
    {{- .Values.database.portOverride }}
  {{- else }}
    {{- if eq .Values.database.provider "mariadb" }}
      {{- .Values.database.defaults.mariadb.port }}
    {{- end }}
    {{- if eq .Values.database.provider "derby" }}
      {{- .Values.database.defaults.derby.port }}
    {{- end }}
    {{- if eq .Values.database.provider "h2" }}
      {{- .Values.database.defaults.h2.port }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create database container port
*/}}
{{- define "api-server.database.containerPort" -}}
  {{- if .Values.database.containerPortOverride }}
    {{- .Values.database.containerPortOverride }}
  {{- else }}
    {{- if eq .Values.database.provider "mariadb" }}
      {{- .Values.database.defaults.mariadb.containerPort }}
    {{- end }}
    {{- if eq .Values.database.provider "derby" }}
      {{- .Values.database.defaults.derby.containerPort }}
    {{- end }}
    {{- if eq .Values.database.provider "h2" }}
      {{- .Values.database.defaults.h2.containerPort }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create database mount path
*/}}
{{- define "api-server.database.mountPath" -}}
  {{- if eq .Values.database.provider "mariadb" }}
    {{- print "/var/lib/mysql" }}
  {{- end }}
  {{- if eq .Values.database.provider "derby" }}
    {{- print "/dbs" }}
  {{- end }}
  {{- if eq .Values.database.provider "h2" }}
    {{- print "" }}
  {{- end }}
{{- end }}