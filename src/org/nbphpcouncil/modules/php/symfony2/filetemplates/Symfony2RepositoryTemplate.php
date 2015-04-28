<?php

<#if repoNamespace?? && repoNamespace != "">
namespace ${repoNamespace};

</#if>

use Doctrine\ORM\EntityRepository;

class ${name} extends EntityRepository
{

}