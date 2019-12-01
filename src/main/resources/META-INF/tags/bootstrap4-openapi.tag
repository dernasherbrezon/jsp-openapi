<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="openapiHelper" uri="https://github.com/dernasherbrezon/jsp-openapi"%>
<%@ attribute name="openapi" required="true" rtexprvalue="true" type="io.swagger.v3.oas.models.OpenAPI"%>
<div class="row">
	<c:choose>
		<c:when test="${!openapi.openapi.startsWith('3.') }">
  			OpenAPI schema is not supported: ${openapi.openapi}
  		</c:when>
		<c:otherwise>
			<div class="col-md-12">
				<h1 class="pb-2 mt-4 mb-2 border-bottom">${openapi.info.title}&nbsp;<span class="badge badge-secondary">${openapi.info.version}</span>
				</h1>
				<p>
					<c:out value="${openapi.info.description}" />
				</p>
				<ul class="list-unstyled">
					<c:if test="${not empty openapi.info.termsOfService}">
						<li><a href="${openapi.info.termsOfService}">Terms of service</a></li>
					</c:if>
					<c:if test="${not empty openapi.info.contact}">
						<li><a href="mailto:${openapi.info.contact.email}">Contact the developer</a></li>
					</c:if>
					<c:if test="${not empty openapi.info.license}">
						<li><a href="${openapi.info.license.url}">${openapi.info.license.name}</a></li>
					</c:if>
					<c:if test="${not empty openapi.externalDocs}">
						<li><a href="${openapi.externalDocs.url}">${openapi.externalDocs.description}</a></li>
					</c:if>
				</ul>
				<p>
				<form>
					<div class="form-row">
						<div class="form-group col-md-12">
							<label for="servers">Servers</label>
							<div class="row">
								<div class="col-md-6">
									<select class="form-control" id="servers">
										<c:forEach items="${openapi.servers}" var="cur">
											<option>${cur.url}</option>
										</c:forEach>
									</select>
								</div>
								<c:if test="${ not empty openapi.components.securitySchemes }">
									<div class="col-md-6">
										<button type="button" class="btn float-right btn-secondary" data-toggle="modal" data-target="#authModal">Authentication</button>
										<div class="modal fade" id="authModal" tabindex="-1" role="dialog" aria-labelledby="authModalLabel" aria-hidden="true">
											<div class="modal-dialog" role="document">
												<div class="modal-content">
													<div class="modal-header">
														<h5 class="modal-title" id="authModalLabel">Authentication</h5>
														<button type="button" class="close" data-dismiss="modal" aria-label="Close">
															<span aria-hidden="true">&times;</span>
														</button>
													</div>
													<div class="modal-body">
														<c:forEach items="${openapi.components.securitySchemes}" var="cur">
															<c:if test="${cur.value.type eq 'APIKEY' }">
																<div>
																	<h3>${cur.key}</h3>
																	<p class="text-muted">${cur.value.type}</p>
																	<p>
																		Name: ${cur.value.name}<br> In: ${cur.value.in}
																	</p>
																</div>
															</c:if>
															<c:if test="${cur.value.type eq 'HTTP' && cur.value.scheme eq 'bearer'}">
																<div>
																	<h3>${cur.key}</h3>
																	<p class="text-muted">${cur.value.type},${cur.value.scheme}</p>
																	<p>
																		In: header<br> Example:
																		<code class="text-white bg-dark">Authorization: Bearer &lt;token&gt;</code>
																	</p>
																</div>
															</c:if>
														</c:forEach>
													</div>
													<div class="modal-footer">
														<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</form>
				</p>
				<div id="accordionTags" role="tablist">
					<c:forEach items="${openapi.tags}" var="cur" varStatus="status">
						<div class="card mb-1">
							<div class="card-header" id="heading${status.index}">
								<h5 class="mb-0">
									<button class="btn btn-link" data-toggle="collapse" data-target="#collapse${status.index}" aria-expanded="false" aria-controls="collapse${status.index}"><strong>${cur.name}</strong></button>
									<span class="text-muted align-middle">${cur.description}</span>
									<c:if test="${not empty cur.externalDocs }">
										<span class="float-right">${cur.externalDocs.description}: <a href="${cur.externalDocs.url}">${cur.externalDocs.url}</a></span>
									</c:if>
								</h5>
							</div>
							<div id="collapse${status.index}" class="collapse" aria-labelledby="heading${status.index}" data-parent="#accordionTags">
								<div class="card-body">
									<div id="accordionTag${status.index}" role="tablist">
										<c:forEach items="${openapiHelper:getMethodsByTag(openapi, cur.name) }" var="method" varStatus="methodStatus">
											<div class="card mb-1">
												<div class="card-header" id="heading${status.index}${methodStatus.index}">
													<h5 class="mb-0">
														<c:if test="${not empty method.operation.security}">
															<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 8 8">
																<path d="M3 0c-1.1 0-2 .9-2 2v1h-1v4h6v-4h-1v-1c0-1.1-.9-2-2-2zm0 1c.56 0 1 .44 1 1v1h-2v-1c0-.56.44-1 1-1z" transform="translate(1)" /></svg>
															&nbsp;									
														</c:if>
														<span class="badge ${openapiHelper:getColorByMethod(method)}">${ method.methodName.toUpperCase()}</span>&nbsp;
														<button class="btn btn-link" data-toggle="collapse" data-target="#collapse${status.index}${methodStatus.index}" aria-expanded="false" aria-controls="collapse${status.index}${methodStatus.index}">
															<c:choose>
																<c:when test="${method.operation.deprecated}">
																	<del>
																		<strong class="text-muted">${method.name}</strong>
																	</del>
																</c:when>
																<c:otherwise>
																	<strong>${method.name}</strong>
																</c:otherwise>
															</c:choose>
														</button>
														&nbsp; <span class="text-muted align-middle">${ method.operation.summary}</span>
													</h5>
												</div>
												<div id="collapse${status.index}${methodStatus.index}" class="collapse" aria-labelledby="heading${status.index}${methodStatus.index}" data-parent="#accordionTag${status.index}">
													<div class="card-body">
														<c:if test="${method.operation.deprecated}">
															<p class="text-muted">Warning: Deprecated</p>
														</c:if>
														<c:if test="${method.operation.description}">
															<p>${method.operation.description}</p>
														</c:if>
														<c:if test="${not empty method.operation.parameters }">
															<div>
																<h4>Parameters</h4>
																<table class="table table-striped">
																	<thead>
																		<tr>
																			<th scope="col" style="width: 20%">Name</th>
																			<th scope="col">Value</th>
																		</tr>
																	</thead>
																	<tbody>
																		<c:forEach items="${method.operation.parameters}" var="curParam">
																			<tr>
																				<td><strong>${ curParam.name }</strong> <c:if test="${curParam.required}">
																						<span class="text-danger"><small>* required</small></span>
																						<br>
																					</c:if> <c:if test="${not empty curParam.schema.type}">
																						<span>${ curParam.schema.type}&nbsp; <c:if test="${curParam.schema.type eq 'array'}">
																								<span>[${ curParam.schema.items.type }]</span>
																							</c:if> <c:if test="${not empty curParam.schema.format }">
																								<span>(${ curParam.schema.format })</span>
																								<br>
																							</c:if>
																						</span>
																					</c:if> <c:if test="${not empty curParam.in }">
																						<span class="text-muted">(${ curParam.in })</span>
																					</c:if></td>
																				<td>
																					<p>${ curParam.description }</p> <c:if test="${ curParam.schema.type eq 'array' && curParam.schema.items.getEnum() != null}">
																						<p>
																							Available values:
																							<c:forEach items="${curParam.schema.items.getEnum()}" var="curEnum" varStatus="enumStatus">
																								<c:if test="${!enumStatus.first}">, </c:if>${curEnum}</c:forEach>
																						</p>
																					</c:if>
																				</td>
																			</tr>
																		</c:forEach>
																	</tbody>
																</table>
															</div>
														</c:if>

														<c:if test="${not empty method.operation.requestBody }">
															<div>
																<h4>
																	Request body
																	<c:if test="${method.operation.requestBody.required}">
																		<span class="text-danger"><small>* required</small></span>
																	</c:if>
																</h4>
																<c:if test="${not empty method.operation.requestBody.description }">
																	<p>${method.operation.requestBody.description}</p>
																</c:if>
																<c:if test="${method.operation.requestBody.content != null && method.operation.requestBody.content['application/json'] != null}">
																	<pre class="text-white bg-dark"><code><openapiHelper:schemaExample openapi="${openapi}" value="${method.operation.requestBody.content['application/json'].schema}"/></code></pre>
																</c:if>
															</div>
														</c:if>
														<h4>Responses</h4>
														<table class="table table-striped">
															<thead>
																<tr>
																	<th scope="col" style="width: 20%">Code</th>
																	<th scope="col">Description</th>
																</tr>
															</thead>
															<tbody>
																<c:forEach items="${method.operation.responses}" var="curResponse">
																	<tr>
																		<td>${curResponse.key}</td>
																		<td>
																			<p>${curResponse.value.description }</p> 
																			<c:if test="${ curResponse.value.content != null && curResponse.value.content['application/json'] != null }">
																				<pre class="text-white bg-dark"><code><openapiHelper:schemaExample openapi="${openapi}" value="${curResponse.value.content['application/json'].schema}"/></code></pre>
																			</c:if> <c:if test="${not empty curResponse.value.headers}">
																				<div>
																					<h6>Headers</h6>
																					<table class="table table-condensed table-hover table-sm">
																						<thead>
																							<tr>
																								<th scope="col" style="width: 20%">Name</th>
																								<th scope="col">Description</th>
																								<th scope="col">Type</th>
																							</tr>
																						</thead>
																						<tbody>
																							<c:forEach items="${curResponse.value.headers}" var="curHeader">
																								<tr>
																									<td>${curHeader.key}</td>
																									<td>${curHeader.value.description}</td>
																									<td>${curHeader.value.schema.type}</td>
																								</tr>
																							</c:forEach>
																						</tbody>
																					</table>
																				</div>
																			</c:if>
																		</td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<hr />
				<h2>Schemas</h2>
				<br>
				<div id="accordionSchemas" role="tablist">
					<c:forEach items="${openapi.components.schemas}" var="cur" varStatus="status">
						<div class="card mb-1">
							<div class="card-header" id="headingSchema${status.index}">
								<h5 class="mb-0">
									<button class="btn btn-link" data-toggle="collapse" data-target="#collapseSchema${status.index}" aria-expanded="false" aria-controls="collapseSchema${status.index}">${cur.key}</button>
								</h5>
							</div>
							<div id="collapseSchema${status.index}" class="collapse" aria-labelledby="headingSchema${status.index}" data-parent="#accordionSchemas">
								<div class="card-body">
									<pre class="text-white bg-dark"><code><openapiHelper:schema value="${cur.value}"/></code></pre>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>
