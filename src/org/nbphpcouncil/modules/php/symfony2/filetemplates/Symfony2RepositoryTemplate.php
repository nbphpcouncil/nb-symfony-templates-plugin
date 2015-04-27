<?php

<#if namespaceDir?? && namespaceDir != "">
namespace ${namespaceDir};

</#if>
use Doctrine\ORM\EntityRepository;

class ${name} extends EntityRepository
{

}