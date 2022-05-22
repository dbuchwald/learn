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
{{- if .Values.mariadb.enabled }}
{{- print "mariadb" }}
{{- else }}
{{- if .Values.derby.enabled }}
{{- print "derby" }}
{{- else -}}
{{- if .Values.h2.enabled }}
{{- print "h2" }}
{{- else }}
{{- print "none" }}
{{- end }}
{{- end }}
{{- end }}
{{- if .Values.test.enabled }}
{{- print ",test" }}
{{- end }}
{{- end }}

{{/*
Create database hostname
*/}}
{{- define "api-server.database.hostname" -}}
{{- printf "%s-database" .Release.Name }}
{{- end }}

{{/*
Create database URL
*/}}
{{- define "api-server.database.url" -}}
  {{- if .Values.mariadb.enabled }}
    {{- printf "%s" (include "api-server.database.hostname" .) }}
    {{- if .Values.mariadb.port }}
      {{- printf ":%s" .Values.mariadb.port }}
    {{- end }}
    {{- printf "/%s" .Values.database.databaseName }}
  {{- else }}
    {{- if .Values.derby.enabled }}
      {{- if .Values.derby.local }}
        {{- printf "/tmp/%s;create=true" .Values.database.databaseName }}
      {{- else }}
        {{- printf "%s" (include "api-server.database.hostname" .) }}
        {{- if .Values.derby.port }}
          {{- printf ":%s" .Values.derby.port }}
        {{- end }}
        {{- printf "/%s" .Values.database.databaseName }}
      {{- end }}
    {{- else }}
      {{- if .Values.h2.enabled }}
        {{- if .Values.h2.local }}
          {{- printf "mem:%s;DB_CLOSE_DELAY=-1" .Values.database.databaseName }}
      {{- else }}
        {{- printf "%s" (include "api-server.database.hostname" .) }}
        {{- if .Values.h2.port }}
          {{- printf ":%s" .Values.h2.port }}
        {{- end }}
        {{- printf "/%s" .Values.database.databaseName }}
      {{- end }}
      {{- else }}
        {{- print "none:none/none" }}
      {{- end }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create database variables for MariaDB pod
*/}}
{{- define "mariadb.databaseName" -}}
{{- if .Values.mariadb.enabled }}
{{- .Values.database.databaseName }}
{{- end }}
{{- end }}