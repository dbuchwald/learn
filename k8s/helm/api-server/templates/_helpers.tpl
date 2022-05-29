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
{{- printf "db-%s" $appName }}
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
{{- .Values.database.provider }}
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
  {{- if eq .Values.database.provider "mariadb" }}
    {{- if .Values.database.embedded }}
      {{- fail "MariaDB provider doesn't support embedded database" }}
    {{- else }}
      {{- printf "//%s" (include "api-server.database.hostname" .) }}
      {{- if .Values.database.port }}
        {{- printf ":%d" (.Values.database.port | int) }}
      {{- end }}
      {{- printf "/%s" .Values.database.databaseName }}
    {{- end }}
  {{- else }}
    {{- if eq .Values.database.provider "derby" }}
      {{- if .Values.database.embedded }}
        {{- printf "/var/apidb/%s;create=true" .Values.database.databaseName }}
      {{- else }}
        {{- printf "//%s" (include "api-server.database.hostname" .) }}
        {{- if .Values.database.port }}
          {{- printf ":%d" (.Values.database.port | int) }}
        {{- end }}
        {{- printf "/%s" .Values.database.databaseName }}
      {{- end }}
    {{- else }}
      {{- if eq .Values.database.provider "h2" }}
        {{- if .Values.database.embedded }}
          {{- printf "/var/apidb/%s;DB_CLOSE_DELAY=-1" .Values.database.databaseName }}
        {{- else }}
          {{- printf "//%s" (include "api-server.database.hostname" .) }}
          {{- if .Values.database.port }}
            {{- printf ":%d" (.Values.database.port | int) }}
          {{- end }}
          {{- printf "/%s" .Values.database.databaseName }}
        {{- end }}
      {{- else }}
        {{- fail "Unsupported database provider" }}
      {{- end }}
    {{- end }}
  {{- end }}
{{- end }}